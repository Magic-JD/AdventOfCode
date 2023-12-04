package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        int current = 1;
        List<String> list = input.stream().map(s -> split(s, ":")[1]).toList();
        Map<Integer, Integer> collect = input.stream().map(s -> split(s, ":")[0]).map(LineUtils::extractInt).collect(Collectors.toMap(i -> i, i -> 1));
        for (String s : list) {
            int times = collect.get(current);
            if (times > 0) {
                String[] split = s.split("\\|");
                var y = split[1];
                var w = split[0];
                List<Integer> yourNo = Arrays.stream(split(y, " ")).filter(x -> !x.isBlank()).map(no -> extractInt(no)).toList();
                List<Integer> wn = Arrays.stream(split(w, " ")).filter(x -> !x.isBlank()).map(no -> extractInt(no)).toList();
                int points = 0;
                for (int n : yourNo) {
                    if (wn.contains(n)) {
                        points++;
                    }
                }
                for (int i = 1; i <= points; i++) {
                    collect.put(i + current, collect.get(i + current) + times);
                }
                if(points == 0 && times == 1){
                    break;
                }
            }
            current++;
        }
        String answer = "" + collect.values().stream().mapToInt(i -> i).sum();
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