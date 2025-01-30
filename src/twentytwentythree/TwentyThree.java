package twentytwentythree;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;

public class TwentyThree {

    public static final String testData = """   
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
                        """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(23);
        run(testInput, "154", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        int height = input.size();
        int width = input.get(0).length();
        char[][] map = new char[height][width];
        updateMap(input, height, map);
        Coord start = new Coord(1, 1);
        Coord end = new Coord(height - 1, width - 2);
        List<Coord> turningPoints = new ArrayList<>(List.of(start));
        updateTurningPoints(height, width, map, turningPoints);

        List<Node> nextNodes = new ArrayList<>();
        initAllNodes(turningPoints, nextNodes, map, end);
        Map<Coord, Node> mapping = nextNodes.stream().collect(Collectors.toMap(n-> n.coord, n -> n));
        ArrayDeque<Walker> walks = new ArrayDeque<>();
        Node startNode = nextNodes.stream().filter(n -> n.coord.equals(start)).findAny().orElseThrow();
        int compensation = 1;
        Node endNode = nextNodes.stream().filter(node -> node.toNodes.containsKey(end)).findAny().orElseThrow();
        compensation+= endNode.toNodes.get(end);
        Walker walker = new Walker(new HashSet<>(), null, startNode, 0);
        walks.add(walker);
        long longestPath = 0;
        while (!walks.isEmpty()) {
            Walker current = walks.pop();
            Node currentNode = current.currentNode;
            for (Map.Entry<Coord, Integer> n : currentNode.toNodes.entrySet()) {
                Node node = mapping.get(n.getKey());
                if(node.equals(endNode)){
                    longestPath = Math.max(longestPath, current.totalDistance + n.getValue());
                } else if(!current.visitedNodes.contains(node) && (current.lastNode == null || !current.lastNode.equals(node))){
                    Walker w = new Walker(new HashSet<>(current.visitedNodes), currentNode, node, current.totalDistance + n.getValue());
                    w.visitedNodes.add(currentNode);
                    walks.addFirst(w);
                }
            }
        }

        var answer = "" + (longestPath + compensation);
        showAnswer(answer, expectedOutput, startTime);
    }

    private static void initAllNodes(List<Coord> turningPoints, List<Node> nextNodes, char[][] map, Coord end) {
        nextNodes.add(new Node(end));
        for (int y = 0; y < turningPoints.size(); y++) {
            Coord tP = turningPoints.get(y);
            Node n = new Node(tP);
            nextNodes.add(n);
            List<Coord> surrounding = getSurrounding(map, tP);
            for (Coord coord : surrounding) {
                int steps = 2;
                Coord last = tP;
                Coord temp = getNext(map, coord, last);
                last = coord;
                Coord next = temp;
                while (!turningPoints.contains(next) && !end.equals(next)) {
                    temp = getNext(map, next, last);
                    last = next;
                    next = temp;
                    steps++;
                }
                n.addCoord(next, steps);
            }
        }
    }

    private static List<Coord> getSurrounding(char[][] map, Coord tP) {
        List<Coord> surrounding = new ArrayList<>();
        int i = tP.x;
        int j = tP.y;
        if (map[i - 1][j] != '#') {
            surrounding.add(new Coord(i - 1, j));
        }
        if (map[i + 1][j] != '#') {
            surrounding.add(new Coord(i + 1, j));
        }
        if (map[i][j + 1] != '#') {
            surrounding.add(new Coord(i, j + 1));
        }
        if (map[i][j - 1] != '#') {
            surrounding.add(new Coord(i, j - 1));
        }
        return surrounding;
    }

    private static void updateTurningPoints(int height, int width, char[][] map, List<Coord> turningPoints) {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                char c = map[i][j];
                if (c != '#') {
                    int count = 0;
                    if (map[i - 1][j] != '#') {
                        count++;
                    }
                    if (map[i + 1][j] != '#') {
                        count++;
                    }
                    if (map[i][j + 1] != '#') {
                        count++;
                    }
                    if (map[i][j - 1] != '#') {
                        count++;
                    }
                    if (count > 2) {
                        turningPoints.add(new Coord(i, j));
                    }
                }
            }
        }
    }

    private static void updateMap(List<String> input, int height, char[][] map) {
        for (int i = 0; i < height; i++) {
            String s = input.get(i)
                    .replaceAll(">", ".")
                    .replaceAll("<", ".")
                    .replaceAll("\\^", ".")
                    .replaceAll("v", ".");
            char[] charArray = s.toCharArray();
            System.arraycopy(charArray, 0, map[i], 0, charArray.length);
        }
        map[0][1] = '#';
    }

    private static Coord getNext(char[][] map, Coord current, Coord last) {
        int i = current.x;
        int j = current.y;
        if (map[i - 1][j] != '#') {
            Coord found = new Coord(i - 1, j);
            if (!found.equals(last)) {
                return found;
            }
        }
        if (map[i + 1][j] != '#') {
            Coord found = new Coord(i + 1, j);
            if (!found.equals(last)) {
                return found;
            }
        }
        if (map[i][j + 1] != '#') {
            Coord found = new Coord(i, j + 1);
            if (!found.equals(last)) {
                return found;
            }
        }
        if (map[i][j - 1] != '#') {
            Coord found = new Coord(i, j - 1);
            if (!found.equals(last)) {
                return found;
            }
        }
        throw new RuntimeException();
    }

    private static void printMap(int height, int width, char[][] map, Node node) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String s = String.valueOf(map[i][j]);
                System.out.print(s);
            }
            System.out.print("\n");
        }
        System.out.println();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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


    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_BLACK = "\u001b[30m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE = "\u001b[37m";

    private record Walker(HashSet<Node> visitedNodes, Node lastNode, Node currentNode, long totalDistance){
    }

    private static class Node {
        private Map<Coord, Integer> toNodes;
        private Coord coord;


        public Node(Coord coord) {
            toNodes = new HashMap<>();
            this.coord = coord;
        }

        public void addCoord(Coord coord, int distance) {
            toNodes.put(coord, distance);
        }
    }

    private record Coord(int x, int y) {
    }
}