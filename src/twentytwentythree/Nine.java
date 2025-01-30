package twentytwentythree;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nine {

    public static final String testData = """         
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
                                 """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(9);
        run(testInput, "2", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = input.stream().map(s -> Arrays.stream(s.split(" "))
                .map(Integer::valueOf).toList())
                .mapToLong(Nine::calculateSeq)
                .sum();
        String answer = "" + sum;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static long calculateSeq(List<Integer> seq){
        if(seq.stream().allMatch(n -> n == 0)){
            return 0;
        } else {
            List<Integer> newSeq = new ArrayList<>();
            for (int i = 0; i < seq.size() - 1; i++) {
                newSeq.add(seq.get(i+1) - seq.get(i));
            }
            return seq.get(0) - calculateSeq(newSeq);
        }
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