package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static tools.LineUtils.extractInt;
import static tools.LineUtils.split;

public class Four {

    public static final String testData = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                        """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(4);
        run(testInput, "30");
        run(mainInput, "???");
    }

    public static void run(List<String> input, String expectedOutput) {
        int[] ticketCounts = IntStream.generate(() -> 1).limit(input.size()).toArray();

        for (int i = 0; i < input.size(); i++) {
            String[] numberStrings = input.get(i).split(":")[1].replaceAll("\\|", "").split("\\s+");
            int points = numberStrings.length - Arrays.stream(numberStrings).collect(Collectors.toSet()).size();
            int times = ticketCounts[i];
            for (int j = 1; j <= points; j++) {
                ticketCounts[j+i] += times;
            }
            if (points == 0 && times == 1) {
                break;
            }
        }
        String answer = "" + Arrays.stream(ticketCounts).sum();
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