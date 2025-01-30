package tasks;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Nineteen {

    public static final String testData = """
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
            """;

    private static final HashMap<String, Long> cache = new HashMap<>();

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(19);
        //run(testInput, "16", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<Towel> towels = Arrays.stream(input.get(0).split(",")).map(String::trim).map(Towel::new).toList();
        List<Pattern> patterns = input.stream().skip(2).map(Pattern::new).toList();
        long count = patterns.stream().mapToLong(pattern -> patternPossible(pattern, towels)).sum();
        showAnswer(String.valueOf(count), expectedOutput, startTime);
    }

    private static long patternPossible(Pattern pattern, List<Towel> towels) {
        if(cache.containsKey(pattern.colors)){
            return cache.get(pattern.colors);
        }
        if(pattern.colors.isBlank()){
            return 1;
        }
        long result = towels.stream()
                .filter(towel -> pattern.colors.startsWith(towel.colors)).mapToLong(towel -> patternPossible(new Pattern(pattern.colors.replaceFirst(towel.colors, "")), towels)).sum();
        cache.put(pattern.colors, result);
        return result;
    }

    private record Towel(String colors){}
    private record Pattern(String colors){}

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
