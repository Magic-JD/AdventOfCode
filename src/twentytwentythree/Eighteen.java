package twentytwentythree;

import tools.InternetParser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eighteen {

    public static final String testData = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
                                           """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(18);
        run(testInput, "952408144115", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private record Point(long i, long j) {
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long i = 0;
        long j = 0;
        long totalMovement = 0;
        List<Point> points = new ArrayList<>();
        for (String str : input) {
            String[] s = str.split(" ");
            String color = s[2].replace("(", "").replace(")", "");
            long move = Integer.valueOf(color.substring(1, 6), 16);
            totalMovement += move;
            Movement m = Movement.values()[Integer.parseInt(color.substring(6))];
            switch (m) {
                case U -> i -= move;
                case R -> j += move;
                case D -> i += move;
                case L -> j -= move;
            }
            points.add(new Point(i, j));
        }
        long count = 0L;
        ArrayDeque<Point> queue = new ArrayDeque<>(points);
        Point first = queue.peek();
        while (!queue.isEmpty()){
            Point p = queue.pop();
            Point p2 = queue.isEmpty() ? first : queue.peek();
            count += (p.i - p2.i) * (p.j + p2.j);
        }
        long edgeAddition = 1 + (totalMovement/2);
        var answer = ""+((Math.abs(count)/2) + edgeAddition);

        showAnswer(answer, expectedOutput, startTime);
    }

    private enum Movement {
        R, D, L, U
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