package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Nice {

    public static final String testData = """
2333133121414131402
                        """;

    //00...111...2...333.44.5555.6666.777.888899

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(9);
        run(testInput, "2858", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        char[] charArray = input.getFirst().toCharArray();
        int length = charArray.length;
        if(length % 2 == 0){
            throw new RuntimeException();
        }
        long sum = 0;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = length-1; i > 0 ; i -= 2) {
            int index = i;
            int size = charArray[i] - '0';
            for (int j = 1; j < i; j+= 2) {
                int emptySize = charArray[j] - '0';
                if(emptySize >= size) {
                    charArray[j] = (char) ((emptySize - size) + '0');
                    charArray[i] = '0';
                    List<Integer> integers = map.computeIfAbsent(j, ewf -> new ArrayList<>());
                    integers.add(size);
                    index = j;
                    break;
                }
            }
            int startingPosition = 0;
            for (int j = 0; j < index; j++) {
                startingPosition += (charArray[j] - '0');
            }
            for (Map.Entry<Integer, List<Integer>> e : map.entrySet()){
                if (e.getKey() <= index){
                    ArrayList<Integer> integers = new ArrayList<>(e.getValue().stream().toList());
                    if(index == e.getKey()) {
                        integers.removeLast();
                    }
                    startingPosition += integers.stream().mapToInt(x -> x).sum();
                }
            }
            int id = (i / 2);
            for (int j = 0; j < size; j++) {
                sum += ((long) id * (startingPosition + j));
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
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
