package twentytwentythree;

import tools.InternetParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fourteen {

    public static final String testData = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
                                           """;




    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(14);
        run(testInput, "64", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        int sum = 0;
        var map = new HashMap<String, Integer>();
        char[][] table =
                input.stream().map(String::toCharArray).toArray(char[][]::new);
        int height = table.length;
        int width = table[0].length;
        int iterationNumber = 1000000000;
        for (int i = 1; i < iterationNumber; i++) {
            table = doCycle(table, height, width);
            String key = toString(table, height);
            Integer firstIndex = map.get(key);
            if(firstIndex != null){
                int cyclePosition = i;
                int goal = iterationNumber - firstIndex;
                int index = goal % (cyclePosition - firstIndex);
                String[] s = map.entrySet().stream().filter(e ->
                        e.getValue() == (index +
                                firstIndex)).findAny().map(Map.Entry::getKey).orElseThrow().split("(?<=\\G.{"
                        + width + "})");
                char[][] array =
                        Arrays.stream(s).map(String::toCharArray).toArray(char[][]::new);
                int answerN = calculateLoad(array, height, width);
                var answer = "" + answerN;
                showAnswer(answer, expectedOutput, startTime);
                return;
            }
            map.put(key, i);
        }
    }

    private static char[][] doCycle(char[][] table, int height, int
            width) {
        for (Direction d : Direction.values()) {
            switch (d) {
                case N -> {
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            char c = table[i][j];
                            if (c == 'O') {
                                int ci = i;
                                int cj = j;
                                while (ci != 0 && table[ci - 1][cj] ==
                                        '.') {
                                    table[ci][cj] = '.';
                                    table[ci - 1][cj] = c;
                                    ci--;
                                }
                            }
                        }
                    }
                }
                case E -> {
                    for (int i = 0; i < height; i++) {
                        for (int j = width - 1; j >= 0; j--) {
                            char c = table[i][j];
                            if (c == 'O') {
                                int ci = i;
                                int cj = j;
                                while (cj < width - 1 && table[ci][cj +
                                        1] == '.') {
                                    table[ci][cj] = '.';
                                    table[ci][cj + 1] = c;
                                    cj++;
                                }
                            }
                        }
                    }
                }
                case S -> {
                    for (int i = height - 1; i >= 0; i--) {
                        for (int j = 0; j < width; j++) {
                            char c = table[i][j];
                            if (c == 'O') {
                                int ci = i;
                                int cj = j;
                                while (ci < height - 1 && table[ci +
                                        1][cj] == '.') {
                                    table[ci][cj] = '.';
                                    table[ci + 1][j] = c;
                                    ci++;
                                }
                            }
                        }
                    }
                }
                case W -> {
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            char c = table[i][j];
                            if (c == 'O') {
                                int ci = i;
                                int cj = j;
                                while (cj != 0 && table[ci][cj - 1] ==
                                        '.') {
                                    table[ci][cj] = '.';
                                    table[ci][cj - 1] = c;
                                    cj--;
                                }
                            }
                        }
                    }
                }
            }
        }
        return table;
    }

    private static int calculateLoad(char[][] table, int height, int
            width) {
        int sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char c = table[i][j];
                if (c == 'O') {
                    sum += height - i;
                }
            }
        }
        return sum;
    }

    private static String toString(char[][] table, int height) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            stringBuilder.append(new String(table[i]));
        }
        return stringBuilder.toString();
    }

    private enum Direction {
        N, W, S, E
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
