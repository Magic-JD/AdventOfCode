package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Stream;

public class Twelve2 {

    public static final String testData = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            ?????????????? 1,1
            """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(12);
        run(testInput, "525152", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private record Spring(String condition, List<Integer> record) {
    }

    ;

    public static void run(List<String> input, String expectedOutput, long startTime) {
        var springs = input.stream().map(s -> s.split(" "))
                .map(s -> {
                    List<Integer> list = Arrays.stream(s[1].split(",")).map(Integer::parseInt).toList();
  //                  return new Spring(s[0] + s[0] + s[0] + s[0] + s[0], Stream.of(list, list, list, list, list).flatMap(Collection::stream).toList());
                    return new Spring(s[0], Stream.of(list).flatMap(Collection::stream).toList());
                });

        var answer = "" + springs.mapToLong(Twelve2::numberOfCombinations).sum();
        showAnswer(answer, expectedOutput, startTime);
    }

    private static long numberOfCombinations(Spring spring) {
        System.out.println(spring.condition + " " + Arrays.toString(spring.record.toArray()));
        char[] condition = spring.condition.toCharArray();
        int currentKnown = 0;
        for (int i = 0; i < condition.length; i++) {
            if (condition[i] == '#') {
                currentKnown++;
            }
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < condition.length; i++) {
            char c = condition[i];
            if (c == '?') {
                indexes.add(i);
            }
        }
        int remainingToFind = spring.record.stream().mapToInt(i -> i).sum() - currentKnown;
        long powRemainingToFind = (long) (Math.pow(2, remainingToFind));
        if (remainingToFind == 0) {
            return 1;
        }
        long max = (long) Math.pow(2, indexes.size()) - 1;
        long start = powRemainingToFind - 1;
        long count = 0;
        while (start <= max){
            count += countNonRecursive(start, remainingToFind, spring.record, indexes, condition) ? 1 : 0;
            long c = start & -start;
            long r = start + c;
            start = (((r^start) >> 2) / c) | r;

        }

        System.out.println(count);
        return count;
    }

    private static boolean countNonRecursive(long check, long remainingToFind, List<Integer> needed, List<Integer> indexes, char[] condition){
        for (int j = 0; j < indexes.size(); j++) {
            String binary = Long.toBinaryString(check);
            condition[indexes.get(j)] = binary.length() <= j ||
                    binary.charAt((binary.length() - 1) - j) == '0' ? '.' : '#';
        }
        return fitsCombination(new String(condition), needed);
    }

    private static boolean fitsCombination(String values, List<Integer> needed) {
        List<Integer> list =
                Arrays.stream(values.split("\\.+")).filter(s ->
                        !s.isEmpty()).map(String::length).toList();
        return list.equals(needed);
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