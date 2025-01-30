package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwentyThree {

    public static final String testData = """
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(23);
        run(testInput, "co,de,ka,ta", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private static class Computer{
        String id;
        List<Computer> connections;

        public Computer(String id, List<Computer> connections){
            this.id = id;
            this.connections = connections;
        }
    }


    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<String, Computer> connections = new HashMap<>();
        input.stream().map(String::trim).map(s -> s.split("-")).forEach(spl -> {
            String firstId = spl[0];
            String secondId = spl[1];
            Computer first = connections.computeIfAbsent(firstId, unused -> new Computer(firstId, new ArrayList<>()));
            Computer second = connections.computeIfAbsent(secondId, unused -> new Computer(secondId, new ArrayList<>()));
            first.connections.add(second);
            second.connections.add(first);
        });
        ArrayDeque<Computer> unexploredComputers = new ArrayDeque<>(connections.values());
        List<Computer> longest = Collections.emptyList();
        Set<Computer> explored = new HashSet<>();
        while(!unexploredComputers.isEmpty()){
            Computer computer = unexploredComputers.pop();
            List<Computer> currentMax = maxList(computer, List.of(computer), new HashSet<>(explored));
            if(currentMax.size() > longest.size()) longest = currentMax;
            explored.add(computer);
        }

        String code = longest.stream().map(c -> c.id).sorted().collect(Collectors.joining(","));
        showAnswer(code, expectedOutput, startTime);
    }

    private static List<Computer> maxList(Computer computer, List<Computer> previous, Set<Computer> explored){
        List<Computer> nextMatches = computer.connections.stream()
                .filter(c -> !explored.contains(c))
                .filter(c -> !previous.contains(c))
                .filter(c -> previous.stream().allMatch(pre -> pre.connections.contains(c)))
                .toList();
        if(nextMatches.isEmpty()){
            return previous;
        }
        return nextMatches.stream().map(c -> {
            List<Computer> current = new ArrayList<>(previous);
            current.add(c);
            List<Computer> computers = maxList(c, current, explored);
            explored.addAll(computers);
            return computers;
        }).max(Comparator.comparing(List::size)).orElse(Collections.emptyList());
    }

    private static void showAnswer(String answer, String expectedOutput, long startTime) {
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
}
