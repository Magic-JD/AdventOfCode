package twentytwentythree;

import tools.InternetParser;

import java.util.*;

import static tools.LineUtils.split;

public class Eight {

    public static final String testData = """         
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
                                 """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(8);
        run(testInput, "6", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        String instructions = input.get(0);
        Map<String, List<String>> route = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            String[] split = split(line, " = ");
            var key = split[0];
            var value = Arrays.stream(split[1].replaceAll("\\(", "").replaceAll("\\)", "").split(", ")).toList();
            route.put(key, value);
        }
        long count = 0;
        ArrayDeque<String> routes = new ArrayDeque<>(route.keySet().stream().filter(s -> s.charAt(2) == 'A').toList());
        var lengths = new ArrayList<Long>();
        List<String> temp = new ArrayList<>();
        while (!routes.isEmpty() && count < 100000) {
            String currentRoute = routes.pop();
            if (!(currentRoute.charAt(2) == 'Z')) {
                var values = route.get(currentRoute);
                char direction = instructions.charAt((int) (count % instructions.length()));
                if (direction == 'L') {
                    temp.add(values.get(0));
                } else {
                    temp.add(values.get(1));
                }
            } else {
                lengths.add(count);
            }

            if (routes.isEmpty()) {
                if (temp.stream().anyMatch(s -> s.equals("DGZ"))) {
                    for (String s : temp) {
                        if (s.charAt(2) == 'Z') {
                            // System.out.println((count + 1) + " : " + s);
                        }
                    }


                }
                routes = new ArrayDeque<>(temp);
                temp = new ArrayList<>();
                count++;
            }

        }
//        int biggest = (int) Math.sqrt(lengths.stream().mapToLong(l -> l).min().orElseThrow());
//        int commonMultiple = 1;
//
//        for(int i =2; i<= biggest; i++){
//            int finalI = i;
//            if(lengths.stream().allMatch(l -> l % finalI == 0)){
//                commonMultiple = i;
//            }
//        }
//        BigInteger sum = BigInteger.ONE;
//        for (Long n : lengths) {
//            sum = sum.multiply(BigInteger.valueOf(n).divide(BigInteger.valueOf(commonMultiple)));
//        }
        lengths.stream().forEach(System.out::println);
        long min = lengths.stream().mapToLong(l -> l).max().orElseThrow();
        lengths.remove(min);
        long sum = min;
        boolean found = false;

        while (!found) {
            long finalSum = sum;
            found = true;
            for (Long length : lengths) {
                if (finalSum % length != 0) {
                    found = false;
                    break;
                }
            }
            sum += min;
        }
        String answer = "" + (sum - min);
        showAnswer(answer, expectedOutput, startTime);
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