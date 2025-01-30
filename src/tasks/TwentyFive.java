package tasks;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwentyFive {

    public static final String testData = """
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(25);
        run(testInput, "3", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private record Key(int a, int b, int c, int d, int e){}
    private record Lock(int a, int b, int c, int d, int e){}

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long count = 0;
        List<Lock> locks =  new ArrayList<>();
        List<Key> keys = new ArrayList<>();
        for (int i = 0; i < input.size(); i+=8) {
            int[] values = new int[5];
            for (int j = 0; j < 7; j++) {
                String s = input.get(i+j);
                for (int k = 0; k < 5; k++) {
                    if(s.charAt(k) == '#'){
                        values[k]++;
                    }
                }
            }
            if(input.get(i).startsWith("#")){
                locks.add(new Lock(values[0], values[1], values[2], values[3], values[4]));
            } else {
                keys.add(new Key(values[0], values[1], values[2], values[3], values[4]));
            }
        }
        for (Key key : keys) {
            for (Lock lock : locks) {
                if(
                        key.a + lock.a <= 7 &&
                                key.b + lock.b <= 7 &&
                                key.c + lock.c <= 7 &&
                                key.d + lock.d <= 7 &&
                                key.e + lock.e <= 7
                ){
                    count++;
                }
            }
        }
        showAnswer(String.valueOf(count), expectedOutput, startTime);
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
