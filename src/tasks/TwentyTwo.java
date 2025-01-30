package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;

public class TwentyTwo {

    public static final String testData = """
1
2
3
2024
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(22);
        run(testInput, "23", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<Map<Key, Integer>> maps = input.stream().mapToLong(Long::parseLong).mapToObj(i -> calculateToValue(i, 2000)).toList();
        Set<Key> validKeys = maps.stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet());
        int max = Integer.MIN_VALUE;
        for (Key key : validKeys) {
            max = Math.max(maps.stream().mapToInt(m -> m.getOrDefault(key, 0)).sum(), max);
        }
        showAnswer(String.valueOf(max), expectedOutput, startTime);
    }

    private record Key(int a, int b, int c, int d){}

    private static Map<Key, Integer> calculateToValue(long initialSecret, int repetitions) {
        int[] lastChanges = new int[4];
        int changePos = 0;
        boolean validChanges = false;
        long lastSecret = initialSecret;
        Map<Key, Integer> values = new HashMap<>();
        for (int i = 0; i < repetitions; i++) {
            long multi = initialSecret * 64;
            initialSecret ^= multi;
            initialSecret %= 16777216;
            long div = initialSecret / 32;
            initialSecret ^= div;
            initialSecret %= 16777216;
            multi = initialSecret * 2048;
            initialSecret ^= multi;
            initialSecret %= 16777216;
            lastChanges[changePos] = (int) (initialSecret % 10 - lastSecret % 10);
            if(changePos == 3){
                validChanges = true;
            }
            if(validChanges){
                Key key = new Key(lastChanges[(changePos + 4) % 4], lastChanges[(changePos+3) % 4], lastChanges[(changePos+2) % 4],lastChanges[(changePos+1) % 4]);
                long finalInitialSecret = initialSecret;
                values.computeIfAbsent(key, unused -> (int) (finalInitialSecret % 10));
            }
            changePos++;
            changePos %= 4;
            lastSecret = initialSecret;
        }
        return values;
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
