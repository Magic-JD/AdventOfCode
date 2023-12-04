
import tasks.Three;
import tools.InternetParser;
import tools.LineUtils;

import java.util.*;

public class Main {

    public static final String testData = """
                        """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(4);
        run(testInput, "-");
        run(mainInput, "???");
    }

    public static void run(List<String> input, String expectedOutput) {
        String answer = "";
        showAnswer(answer, expectedOutput);
    }

    public static void showAnswer(String answer, String expectedOutput) {
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
    }

}