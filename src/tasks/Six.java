package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.util.Arrays;
import java.util.List;

public class Six {

    public static final String testData = """         
            Time:      7  15   30
            Distance:  9  40  200             
                     """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(6);
        run(testInput, "71503", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long time = LineUtils.extractLong(input.get(0).split(":")[1]);
        long distance = LineUtils.extractLong(input.get(1).split(":")[1]);
        int sum = 1;
        int noBeating = 0;

        for (int j = 1; j < time; j++) {
            long total = j * (time - j);
            if (total > distance) {
                noBeating++;
            }


        }
        String answer = "" + noBeating;
        showAnswer(answer, expectedOutput, startTime);
    }

    public static void showAnswer(String answer, String expectedOutput, long startTime) {
        if (expectedOutput.equals("???")) {
            System.out.println("The actual output is : " + answer);
        } else {
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