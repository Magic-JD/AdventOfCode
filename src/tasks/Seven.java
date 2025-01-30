package tasks;

import tools.Direction;
import tools.InternetParser;

import java.util.*;
import java.util.stream.Stream;

public class Seven {

    public static final String testData = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(7);
        run(testInput, "11387", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = input.stream()
                .map(s -> s.split(":"))
                .map(s -> new Sum(Long.parseLong(s[0]), Arrays.stream(s[1].trim().split(" ")).map(Long::parseLong).toList().reversed().stream().mapToLong(i -> i).toArray()))
                .filter(Seven::canMakeSum)
                .mapToLong(s -> s.ans)
                .sum();

        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static boolean canMakeSum(Sum s) {
        if(s.vals.length == 0){
            return s.ans == 0;
        }
        if(s.ans <= 0){
            return false;
        }
        long[] vals = Arrays.copyOfRange(s.vals, 1, s.vals.length);
        boolean summing = canMakeSum(new Sum(s.ans - s.vals[0], vals));
        boolean foe = false;
        String ansString = String.valueOf(s.ans);
        String valsString = String.valueOf(s.vals[0]);
        if(s.vals.length > 1 && ansString.endsWith(valsString)){
            String substring = ansString.substring(0, ansString.length() - (valsString.length()));
            if(!substring.isBlank()){
                foe = canMakeSum(new Sum(Long.parseLong(substring), vals));
            }
        }
        if(s.ans % s.vals[0] != 0){
            return summing || foe;
        }

        return summing || foe || canMakeSum(new Sum(s.ans / s.vals[0], vals));
    }

    private record Sum(long ans, long... vals){}


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
