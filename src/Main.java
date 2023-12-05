
import tasks.Three;
import tools.InternetParser;
import tools.LineUtils;

import java.util.*;

import static tools.LineUtils.split;

public class Main {

    public static final String testData = """
            seeds: 79 14 55 13
                                    
            seed-to-soil map:
            50 98 2
            52 50 48
                                    
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                                    
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                                    
            water-to-light map:
            88 18 7
            18 25 70
                                    
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                                    
            temperature-to-humidity map:
            0 69 1
            1 0 69
                                    
            humidity-to-location map:
            60 56 37
            56 93 4
                        """;

    private static record MR(long start, long end) {
    }

    private static class Range {
        private long destStart;
        private long sourceStart;
        private long range;

        public Range(long destStart, long sourceStart, long range) {
            this.destStart = destStart;
            this.sourceStart = sourceStart;
            this.range = range;
        }

        public List<List<MR>> fallsInRange(MR mr) {
            var conStart = mr.start - sourceStart;
            var conEnd = mr.end - sourceStart;
            if (conEnd < 0 || conStart >= range) { //>= or >?
                return Collections.emptyList();
            }
            if (conStart >= 0 && conEnd < range) { // here too?
                return List.of(List.of(new MR(destStart + conStart, destStart + conEnd)), Collections.emptyList()); //Correct
            }
            if (conStart >= 0) {
                var remainingRange = conEnd - range;
                return List.of(List.of(new MR(destStart + conStart, destStart + (conEnd - remainingRange - 1))), List.of(new MR(mr.start + (range - conStart), mr.end))); //Correct but not 100% sure on the last range
            }
            if (conEnd < range) {
                var remainingRange = Math.abs(conStart);
                return List.of(List.of(new MR(destStart, destStart + (mr.end - mr.start - remainingRange))), List.of(new MR(mr.start, mr.start + remainingRange - 1)));//Correct
            } else {
                var remainingRange1 = conEnd - range;
                return List.of(List.of(new MR(destStart, (destStart + range) - 1)), List.of(new MR(mr.start, mr.start + remainingRange1 - 1), new MR(mr.start + (range - conStart), mr.end)));
            }
        }
    }

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(5);
        run(testInput, "46", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        var seeds = Arrays.stream(split(input.get(0).split(":")[1], " ")).map(LineUtils::extractLong).toList();
        ArrayDeque<MR> newValues = new ArrayDeque<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            long first = seeds.get(i);
            long second = seeds.get(i + 1);
            newValues.add(new MR(first, first + second));
        }
        List<List<Range>> mappings = new ArrayList<>();
        List<Range> current = new ArrayList<>();
        for (int i = 3; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isBlank()) {
                mappings.add(current);
                current = new ArrayList<>();
                i += 1;
            } else {
                var split = Arrays.stream(line.split(" ")).map(LineUtils::extractLong).toArray(Long[]::new);
                current.add(new Range(split[0], split[1], split[2]));
            }
        }
        mappings.add(current);
        List<MR> mrs = new ArrayList<>();
        for (List<Range> ranges : mappings) {
            while (!newValues.isEmpty()){
                MR seed = newValues.remove();
                boolean isUpdated = false;
                for (Range range : ranges) {
                    var list = range.fallsInRange(seed);
                    if (!list.isEmpty()) {
                        mrs.addAll(list.get(0));
                        if(!list.get(1).isEmpty()){
                            newValues.addAll(list.get(1));
                        }
                        isUpdated = true;
                        break;
                    }
                }
                if(!isUpdated){
                    mrs.add(seed);
                }
            }
            newValues = new ArrayDeque<>(mrs);
            mrs = new ArrayList<>();
        }

        String answer = newValues.stream().mapToLong(m -> m.start).min().orElseThrow() + "";
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