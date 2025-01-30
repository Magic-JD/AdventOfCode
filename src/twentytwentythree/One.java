package twentytwentythree;

import tools.FileParser;

import java.util.List;
import java.util.Optional;

public class One {
    public static void main(String[] args) {
        FileParser fileParser = new FileParser();
        List<String> strings = fileParser.parseFile("resources/task/_1.txt");
        System.out.println(strings.stream()
                .map(s -> findNumberWordsForwards(Optional.of(s)) + findNumberWordsBackwards(Optional.of(s)))
                .filter(s -> !s.isBlank())
                .map(s -> s.charAt(0) + s.substring(s.length() - 1))
                .mapToInt(Integer::valueOf).sum());
    }

    private static String findNumberWordsForwards(Optional<String> st) {
        return st.map(s -> s.replaceAll("nine", "nine9"))
                .map(s -> s.replaceAll("eight", "eight8"))
                .map(s -> s.replaceAll("seven", "seven7"))
                .map(s -> s.replaceAll("six", "six6"))
                .map(s -> s.replaceAll("five", "five5"))
                .map(s -> s.replaceAll("four", "four4"))
                .map(s -> s.replaceAll("three", "three3"))
                .map(s -> s.replaceAll("two", "two2"))
                .map(s -> s.replaceAll("one", "one1"))
                .map(s -> s.replaceAll("[a-z]", ""))
                .orElse("");
    }

    private static String findNumberWordsBackwards(Optional<String> st) {
        return st.map(s -> s.replaceAll("nine", "9nine"))
                .map(s -> s.replaceAll("eight", "8eight"))
                .map(s -> s.replaceAll("seven", "7seven"))
                .map(s -> s.replaceAll("six", "6six"))
                .map(s -> s.replaceAll("five", "5five"))
                .map(s -> s.replaceAll("four", "4four"))
                .map(s -> s.replaceAll("three", "3three"))
                .map(s -> s.replaceAll("two", "2two"))
                .map(s -> s.replaceAll("one", "1one"))
                .map(s -> s.replaceAll("[a-z]", ""))
                .orElse("");
    }
}