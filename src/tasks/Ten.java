package tasks;

import tools.InternetParser;

import java.util.*;

public class Ten {

    public static final String testData = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
                        """;


    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(10);
        run(testInput, "36", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = 0;
        int[][] map = input.stream().map(String::toCharArray).map(ca -> {
            int[] ints = new int[ca.length];
            for (int i = 0; i < ca.length; i++) {
                ints[i] = ca[i] - '0';
            }
            return ints;
        }).toArray(int[][]::new);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == 0){
                    int trailheads = exploreMap(0, i, j, map);
                    sum += trailheads;
                }
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static int exploreMap(int value, int i, int j, int[][] map) {
        if(value == 9){
            return 1;
        }
        int sum = 0;
        try {
            int x = map[i+1][j];
            if(x == value + 1){
                sum += exploreMap(x, i+1, j, map);
            }
        } catch (IndexOutOfBoundsException e){
        }
        try {
            int x = map[i-1][j];
            if(x == value + 1){
                sum += exploreMap(x, i-1, j, map);
            }
        } catch (IndexOutOfBoundsException e){
        }
        try {
            int x = map[i][j+1];
            if(x == value + 1){
                sum += exploreMap(x, i, j+1, map);
            }
        } catch (IndexOutOfBoundsException e){
        }
        try {
            int x = map[i][j-1];
            if(x == value + 1){
                sum += exploreMap(x, i, j-1, map);
            }
        } catch (IndexOutOfBoundsException e){
        }
        return sum;
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
