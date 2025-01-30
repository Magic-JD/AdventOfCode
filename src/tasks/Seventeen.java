package tasks;

import tools.InternetParser;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Seventeen {

    public static final String testData = """
            Register A: 0
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(17);
        run(testInput, "117440", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<Integer> instructions = Arrays.stream(input.get(4).split(" ")[1].split(",")).map(Integer::parseInt).toList();

        BigInteger a = BigInteger.ONE;
        long b = 0;
        long c = 0;

        for (int i = instructions.size()-1; i >= 0; i--){
            List<Integer> expectedResult = instructions.subList(i, instructions.size());
            List<Integer> output = calculateResult(a, instructions);
            while(!output.equals(expectedResult)){
                a = a.add(BigInteger.ONE);
                output = calculateResult(a, instructions);
            }
            if(i != 0) {
                a = a.multiply(BigInteger.valueOf(8));
            }
        }
        printNumber(a, b, c, instructions);
        showAnswer(a.toString(), expectedOutput, startTime);
    }

    private static List<Integer> calculateResult(BigInteger a, List<Integer> instructions){
        long b = 0;
        long c = 0;
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i += 2) {
            int instr = instructions.get(i);
            int op = instructions.get(i + 1);
            switch (instr) {
                case 0 -> a = (a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))));
                case 1 -> b = b ^ op;
                case 2 -> b = getComboOp(op, a, b, c).mod(BigInteger.valueOf(8)).longValue();
                case 3 -> {
                    if (!a.equals(BigInteger.ZERO)) i = op - 2;
                }
                case 4 -> b = b ^ c;
                case 5 -> {
                    result.add(getComboOp(op, a, b, c).mod(BigInteger.valueOf(8)).intValue());
                }
                case 6 -> b = a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))).longValue();
                case 7 -> c = a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))).longValue();
                default -> throw new RuntimeException();
            }
        }
        return result;
    }

    private static void printNumber(BigInteger a, long b, long c, List<Integer> instructions){
        System.out.println("Expected output for " + a.toString());
        System.out.println(instructions.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        System.out.println("Actual output for " + a.toString());
        for (int i = 0; i < instructions.size(); i += 2) {
            int instr = instructions.get(i);
            int op = instructions.get(i + 1);
            switch (instr) {
                case 0 -> a = (a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))));
                case 1 -> b = b ^ op;
                case 2 -> b = getComboOp(op, a, b, c).mod(BigInteger.valueOf(8)).longValue();
                case 3 -> {
                    if (!a.equals(BigInteger.ZERO)) i = op - 2;
                }
                case 4 -> b = b ^ c;
                case 5 -> {
                    System.out.print(getComboOp(op, a, b, c).mod(BigInteger.valueOf(8)).intValue());
                    System.out.print(", ");
                }
                case 6 -> b = a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))).longValue();
                case 7 -> c = a.divide(BigInteger.valueOf((long) Math.pow(2, getComboOp(op, a, b, c).longValue()))).longValue();
                default -> throw new RuntimeException();
            }
        }
        System.out.println();
    }
    private static BigInteger getComboOp(int op, BigInteger a, long b, long c) {
        return switch (op) {
            case 0, 1, 2, 3 -> BigInteger.valueOf(op);
            case 4 -> a;
            case 5 -> BigInteger.valueOf(b);
            case 6 -> BigInteger.valueOf(c);
            default -> throw new RuntimeException();
        };
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

    private record JumpState(long a, long b, long c, int i) {
    }
}
