package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Stream;

public class Twelve {



    public static final String testData = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
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

    private record Spring(char[] condition, List<Integer> record) {}

    public static void run(List<String> input, String expectedOutput, long startTime) {
        var springs = input.stream().map(s -> s.split(" "))
                .map(s -> {
                    List<Integer> list = Arrays.stream(s[1].split(",")).map(Integer::parseInt).toList();
                    List<Integer> list1 = Stream.of(list, list, list, list, list).flatMap(Collection::stream).toList();
                    return new Spring(preProcess(s[0] + "?" + s[0] + "?" + s[0] + "?" + s[0] + "?" + s[0], list1), list1);
                });

        var answer = "" + springs.parallel().mapToLong(Twelve::numberOfCombinations).sum();
        showAnswer(answer, expectedOutput, startTime);
    }


    private static char[] preProcess(String string, List<Integer> ints){
        char[] processed = string.replaceAll("\\.+", ".").toCharArray();
        return processed;
    }


    private static long numberOfCombinations(Spring spring) {
        char[] condition = spring.condition;
        var record = spring.record.toArray(Integer[]::new);
        int countHash = 0;
        int lastHashIndex = 0;
        for (int i = 0; i < condition.length; i++) {
            if(condition[i] == '#'){
                countHash++;
                lastHashIndex = i;
            }
        }
        long recurse = recurse(condition, record, countHash, spring.record.stream().mapToInt(i->i).sum(), 0, 0, lastHashIndex);
        System.out.println(recurse);
        return recurse;
    }

    private static long recurse(char[] condition, Integer[] record, int hashRemaining, int recordRemaining, int recordIndex, int conditionIndex, int lastHashIndex){
        if(recordIndex == record.length){
            return conditionIndex > lastHashIndex ? 1 : 0;
        }
        if(recordRemaining < hashRemaining){
            return 0;
        }
        if(condition.length - conditionIndex < recordRemaining + ((record.length - recordIndex) - 1)){
            return 0;
        }

        int current = record[recordIndex] + conditionIndex;
        if(condition.length < current){
            return 0;
        }
        if(condition[conditionIndex] == '.'){
            return recurse(condition, record, hashRemaining, recordRemaining, recordIndex, conditionIndex+1, lastHashIndex);
        }
        long count = 0;
        boolean canPlaceHash = true;
        boolean canPlaceDot = true;
        if(condition[conditionIndex] == '#') canPlaceDot = false;
        int countHashInCurrent = 0;
        for (int i = conditionIndex; i < current; i++) {
            if(condition[i] == '.'){
                canPlaceHash = false;
                break;
            }
            if(condition[i] == '#'){
                countHashInCurrent++;
            }
        }
        if(current < condition.length && condition[current] == '#') canPlaceHash = false;
        if(canPlaceDot){
            count += recurse(condition, record, hashRemaining, recordRemaining, recordIndex, conditionIndex+1, lastHashIndex);
        }
        if(canPlaceHash){
            if(current >= condition.length - 1){
                count += recordIndex + 1 == record.length ? 1 : 0;
            } else {
                count += recurse(condition, record, hashRemaining - countHashInCurrent, recordRemaining - (current-conditionIndex), recordIndex+1, current + 1, lastHashIndex);
            }
        }
        return  count;
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