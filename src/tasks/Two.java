package tasks;

import tools.InternetParser;

import java.util.*;

public class Two {

    public static final String testData = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(2);
        run(testInput, "4", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        int size = input.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()).filter(arr -> {
            if(isValid(arr)){
                return true;
            } else {
                for (int i = 0; i < arr.size(); i++) {
                    ArrayList<Integer> newList = new ArrayList<>(arr);
                    newList.remove(i);
                    if(isValid(newList)){
                        return true;
                    }
                }
            }
            return false;

        }).toList().size();
        showAnswer(String.valueOf(size), expectedOutput, startTime);
    }

    private static boolean isValid(List<Integer> arr) {
        boolean asc = arr.get(0) < arr.get(1);
        for (int i = 0; i < arr.size() - 1; i++) {
            int first = arr.get(i);
            int second = arr.get(i + 1);
            int diff = asc ? second - first : first - second;
            if (diff < 1 || diff > 3) {
                return false;
            }
        }
        return true;
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
