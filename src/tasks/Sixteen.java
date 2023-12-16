package tasks;

import tools.Direction;
import tools.InternetParser;

import java.util.*;
import java.util.stream.Stream;

import static tools.Direction.*;

public class Sixteen {

    public static final String testData = """      
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
                                           """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(16);
        run(testInput, "51", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private static Node[][] factory;

    private static class Node {

        private int i;
        private int j;

        private char mirror;
        private boolean energized = false;

        public Node(char mirror) {
            this.mirror = mirror;
        }

        public void energize() {
            energized = true;
        }
    }

    private static class Beam {
        public Beam(Node node, Direction direction) {
            this.node = node;
            this.direction = direction;
        }

        private Node node;
        private Direction direction;

    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        int heigth = input.size();
        int width = input.get(0).length();
        factory = input.stream().map(s -> s.chars().mapToObj(i -> (char) i).map(c -> new Node(c)).toArray(Node[]::new)).toArray(Node[][]::new);
        for (int i = 0; i < heigth; i++) {
            for (int j = 0; j < width; j++) {
                Node node = factory[i][j];
                node.i = i;
                node.j = j;
            }
        }
        int sum = 0;
        for (int i = 0; i < factory.length; i++) {
            for (int j = 0; j < factory[0].length; j += factory[0].length - 1) {
                performOperation(i, j, j == 0 ? E : W);
                sum = Math.max(Arrays.stream(factory).flatMap(arr -> Arrays.stream(arr)).mapToInt(n -> n.energized ? 1 : 0).sum(), sum);
            }
        }
        for (int i = 0; i < factory.length; i+= factory.length-1) {
            for (int j = 0; j < factory[0].length; j ++) {
                performOperation(i, j, i == 0 ? S : N);
                sum = Math.max(Arrays.stream(factory).flatMap(arr -> Arrays.stream(arr)).mapToInt(n -> n.energized ? 1 : 0).sum(), sum);
            }
        }


        var answer = "" + sum;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static void performOperation(int i, int j, Direction d) {
        resetFactory();
        Node startNode = factory[i][j];
        ArrayDeque<Beam> beams = new ArrayDeque<>();
        beams.add(new Beam(startNode, d));
        Map<Node, List<Direction>> seen = new HashMap<>();
        while (!beams.isEmpty()) {
            Beam beam = beams.pop();
            List<Direction> orDefault = seen.getOrDefault(beam.node, new ArrayList<>());
            if (!orDefault.contains(beam.direction)) {
                beam.node.energize();
                orDefault.add(beam.direction);
                seen.put(beam.node, orDefault);
                char c = beam.node.mirror;
                switch (c) {
                    case '.' -> beams.addAll(basicBeamUpdate(beam));
                    case '\\' -> beams.addAll(leftLeanBeamUpdate(beam));
                    case '/' -> beams.addAll(rightLeanBeamUpdate(beam));
                    case '|' -> beams.addAll(verticalBeamSplitter(beam));
                    case '-' -> beams.addAll(horizontalBeamSplitter(beam));
                }
            }
        }
    }

    private static void resetFactory() {
        for (int i = 0; i < factory.length; i++) {
            for (int j = 0; j < factory[0].length; j++) {
                factory[i][j].energized = false;
            }
        }
    }

    private static List<Beam> horizontalBeamSplitter(Beam beam) {
        return switch (beam.direction) {
            case E, W -> basicBeamUpdate(beam);
            case N, S ->
                    Stream.of(basicBeamUpdate(new Beam(beam.node, E)), basicBeamUpdate(new Beam(beam.node, W))).flatMap(Collection::stream).toList();
        };
    }

    private static List<Beam> verticalBeamSplitter(Beam beam) {
        return switch (beam.direction) {
            case N, S -> basicBeamUpdate(beam);
            case E, W ->
                    Stream.of(basicBeamUpdate(new Beam(beam.node, N)), basicBeamUpdate(new Beam(beam.node, S))).flatMap(Collection::stream).toList();
        };
    }

    private static List<Beam> rightLeanBeamUpdate(Beam beam) {
        return switch (beam.direction) {
            case N -> basicBeamUpdate(new Beam(beam.node, E));
            case E -> basicBeamUpdate(new Beam(beam.node, N));
            case S -> basicBeamUpdate(new Beam(beam.node, W));
            case W -> basicBeamUpdate(new Beam(beam.node, S));
        };
    }

    private static List<Beam> leftLeanBeamUpdate(Beam beam) {
        return switch (beam.direction) {
            case N -> basicBeamUpdate(new Beam(beam.node, W));
            case E -> basicBeamUpdate(new Beam(beam.node, S));
            case S -> basicBeamUpdate(new Beam(beam.node, E));
            case W -> basicBeamUpdate(new Beam(beam.node, N));
        };
    }

    private static List<Beam> basicBeamUpdate(Beam beam) {
        return switch (beam.direction) {
            case N ->
                    beam.node.i > 0 ? List.of(new Beam(factory[beam.node.i - 1][beam.node.j], N)) : Collections.emptyList();
            case E ->
                    beam.node.j < factory[0].length - 1 ? List.of(new Beam(factory[beam.node.i][beam.node.j + 1], E)) : Collections.emptyList();
            case S ->
                    beam.node.i < factory.length - 1 ? List.of(new Beam(factory[beam.node.i + 1][beam.node.j], S)) : Collections.emptyList();
            case W ->
                    beam.node.j > 0 ? List.of(new Beam(factory[beam.node.i][beam.node.j - 1], W)) : Collections.emptyList();
        };
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

}