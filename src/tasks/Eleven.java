package tasks;

import tools.InternetParser;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

public class Eleven {

    public static final String testData = """
125 17
""";

    private static final Map<NfD, BigInteger> cache = new HashMap<>();


    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(11);
        run(testInput, "55312", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<BigInteger> longList = Arrays.stream(input.getFirst().split(" ")).map(BigInteger::new).toList();
        BigInteger reduce = longList.stream().map(l -> sizeForNumber(l, 74)).reduce(BigInteger.ZERO, BigInteger::add);
        showAnswer(reduce.toString(), expectedOutput, startTime);
    }

    private static BigInteger sizeForNumber(BigInteger number, int depth){
        NfD key = new NfD(number, depth);
        BigInteger cached = cache.get(key);
        if(cached != null){
            return cached;
        }
        if(depth == 0){
            BigInteger answer = number.toString().length() % 2 == 0 ? BigInteger.valueOf(2) : BigInteger.valueOf(1);
            cache.put(key, answer);
            return answer;
        }
        if(number.equals(BigInteger.ZERO)){
            BigInteger answer = sizeForNumber(BigInteger.ONE, depth-1);
            cache.put(key, answer);
            return answer;
        }

        String s = number.toString();
        if(s.length() % 2 == 0){
            BigInteger result = sizeForNumber(new BigInteger(s.substring(0, s.length() / 2)), depth - 1).add(sizeForNumber(new BigInteger(s.substring(s.length() / 2)), depth - 1));
            cache.put(key, result);
            return result;
        }
        BigInteger result = sizeForNumber(number.multiply(BigInteger.valueOf(2024L)), depth -1);
        cache.put(key, result);
        return result;
    }

    private record NfD(BigInteger number, int depth){}


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
