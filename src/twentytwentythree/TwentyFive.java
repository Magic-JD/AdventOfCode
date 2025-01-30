package twentytwentythree;

import tools.InternetParser;
import tools.LineUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TwentyFive {

    public static final String testData = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
                                           """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(25);
        run(testInput, "54", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Set<String> names = input.stream().map(s -> s.replaceAll(":", "")).flatMap(s -> Arrays.stream(LineUtils.split(s, " "))).collect(Collectors.toSet());
        Map<String, Node> namesToNodes = new HashMap<>();
        List<Connection> allConnections = new ArrayList<>();
        for (String name : names) {
            namesToNodes.put(name, new Node(name, new ArrayList<>()));
        }
        for (String string : input) {
            String[] split = string.split(":");
            Node connector = namesToNodes.get(split[0]);
            Arrays.stream(LineUtils.split(split[1], " ")).map(namesToNodes::get).forEach(node -> {
                Connection c = new Connection(connector, node);
                allConnections.add(c);
                connector.connections.add(c);
                node.connections.add(c);
            });
        }
        boolean solved = false;
        List<String> orderedName = new ArrayList<>(names);
        long totalValue = 0L;
        for (String string : orderedName) {
            allConnections.forEach(c -> c.canAccess = true);
            Node startNode = namesToNodes.get(string);
            List<Node> visited = new ArrayList<>(List.of(startNode));
            ArrayDeque<Node> queue = new ArrayDeque<>(visited);
            Node endNode = null;

            while (!queue.isEmpty()) {
                Node current = queue.pop();
                endNode = current;
                List<Node> list = current.connections.stream().map(c -> c.findNext(current)).filter(n -> !visited.contains(n)).toList();
                visited.addAll(list);
                queue.addAll(list);
            }
            for (int i = 0; i <= 3; i++) {
                List<Connection> connections = new ArrayList<>(startNode.connections.stream().filter(c -> c.canAccess).toList());
                ArrayDeque<List<Connection>> connectionQueue = new ArrayDeque<>(connections.stream().map(List::of).toList());
                List<Node> visitiedNodes = new ArrayList<>(List.of(startNode));
                boolean found = false;
                while (!connectionQueue.isEmpty()){
                    List<Connection> pop = connectionQueue.pop();
                    Connection lastConnection = pop.getLast();
                    Node nextNode = visitiedNodes.contains(lastConnection.nodeA) ? lastConnection.nodeB : lastConnection.nodeA;
                    if(!visitiedNodes.contains(nextNode)){
                        if(nextNode.equals(endNode)){
                            found = true;
                            connections = pop;
                            break;
                        } else {
                            visitiedNodes.add(nextNode);
                            nextNode.connections.stream().filter(c -> c.canAccess).filter(c -> !visitiedNodes.contains(c.findNext(nextNode))).forEach(c -> {
                                List<Connection> newC = new ArrayList<>(pop);
                                newC.add(c);
                                connectionQueue.add(newC);
                            });
                        }
                    }
                }
                if(!found){
                    List<Connection> blockedConnections = new ArrayList<>(allConnections.stream().filter(connection -> !connection.canAccess).toList());
                    List<Connection> keyConnection = new ArrayList<>();
                    while (keyConnection.size() < 3){
                        Connection removed = blockedConnections.remove(0);
                        removed.canAccess = true;

                        ArrayDeque<List<Connection>> connectionQueue2 = new ArrayDeque<>(connections.stream().map(List::of).toList());
                        List<Node> visitiedNodes2 = new ArrayList<>(List.of(startNode));
                        boolean found2 = false;
                        while (!connectionQueue2.isEmpty()){
                            List<Connection> pop = connectionQueue2.pop();
                            Connection lastConnection = pop.getLast();
                            Node nextNode = visitiedNodes2.contains(lastConnection.nodeA) ? lastConnection.nodeB : lastConnection.nodeA;
                            if(!visitiedNodes2.contains(nextNode)){
                                if(nextNode.equals(endNode)){
                                    found2 = true;
                                    break;
                                } else {
                                    visitiedNodes2.add(nextNode);
                                    nextNode.connections.stream().filter(c -> c.canAccess).filter(c -> !visitiedNodes2.contains(c.findNext(nextNode))).forEach(c -> {
                                        List<Connection> newC = new ArrayList<>(pop);
                                        newC.add(c);
                                        connectionQueue2.add(newC);
                                    });
                                }
                            }

                        }
                        if(found2){
                            removed.canAccess = false;
                            keyConnection.add(removed);
                        }
                    }
                    List<Node> visited2 = new ArrayList<>(List.of(startNode));
                    ArrayDeque<Node> queue2 = new ArrayDeque<>(visited2);

                    while (!queue2.isEmpty()) {
                        Node current = queue2.pop();
                        List<Node> list = current.connections.stream().filter(c -> c.canAccess).map(c -> c.findNext(current)).filter(n -> !visited2.contains(n)).toList();
                        visited2.addAll(list);
                        queue2.addAll(list);
                    }
                    int size = visited2.size();
                    totalValue = (long) size * (names.size() - size);

                } else {
                   connections.forEach(c -> c.canAccess = false);
                }

            }
            System.out.println(startNode);
            System.out.println(endNode);
            if(totalValue > 0L){
                break;
            }

        }
        var answer = "" + totalValue;
        showAnswer(answer, expectedOutput, startTime);
    }

    public static void showAnswer(String answer, String expectedOutput, long startTime) {
        if (expectedOutput.equals("???")) {
            System.out.println("ACTUAL ANSWER");
            System.out.println("The actual output is : " + answer);
        } else {
            System.out.println("TEST CASE");
            System.out.println("Current answer = " + answer + ". Expected answer = " + expectedOutput);
            if (answer.equals(expectedOutput)) {
                System.out.println("CORRECT");
            } else {
                System.out.println("INCORRECT");
            }
        }
        System.out.println("Runtime: " + (System.currentTimeMillis() - startTime));
        System.out.println("-----------------------------------------------------------");
    }

    private record Node(String name, List<Connection> connections){}

    private static final class Connection {
        private final Node nodeA;
        private final Node nodeB;
        private boolean canAccess = true;

        private Connection(Node nodeA, Node nodeB) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
        }

        public Node findNext(Node node) {
                if (nodeA.equals(node)) {
                    return nodeB;
                } else if (nodeB.equals(node)) {
                    return nodeA;
                } else {
                    throw new RuntimeException();
                }
            }

        public Node nodeA() {
            return nodeA;
        }

        public Node nodeB() {
            return nodeB;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Connection) obj;
            return Objects.equals(this.nodeA.name, that.nodeA.name) &&
                    Objects.equals(this.nodeB.name, that.nodeB.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeA, nodeB);
        }

        @Override
        public String toString() {
            return "Connection[" +
                    "nodeA=" + nodeA.name + ", " +
                    "nodeB=" + nodeB.name + ']';
        }

        }

}