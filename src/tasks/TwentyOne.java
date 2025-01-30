package tasks;

import tasks.keypad.DirectionKeypad;
import tasks.keypad.Keypad;
import tasks.keypad.NumberKeypad;
import tools.InternetParser;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TwentyOne {

    public static final String testData = """
            379A
            029A
            980A
            179A
            456A
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(21);
        run(testInput, "126384", System.currentTimeMillis(), 2);
        run(mainInput, "136780", System.currentTimeMillis(), 2);
        run(mainInput, "190245188358888", System.currentTimeMillis(), 25);
    }

    public static void run(List<String> input, String expectedOutput, long startTime, int intermediateRobots) {
        Keypad nk = new NumberKeypad();
        DirectionKeypad directionKeypad = new DirectionKeypad(null);
        for (int i = 0; i < intermediateRobots-1; i++) {
            directionKeypad = new DirectionKeypad(directionKeypad);
        }
        BigInteger total = BigInteger.ZERO;
        for(String s : input){
            int multiplier = Integer.parseInt(s.substring(0, s.length()-1));
            DirectionKeypad finalDirectionKeypad = directionKeypad;
            BigInteger current = s.chars().flatMap(c -> nk.move((char) c).getFirst().chars()).mapToObj(c -> finalDirectionKeypad.count((char) c)).reduce(BigInteger.ZERO, BigInteger::add);

            total = total.add(current.multiply(BigInteger.valueOf(multiplier)));
        }
        showAnswer(total.toString(), expectedOutput, startTime);
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
