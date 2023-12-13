package tasks;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thirteen {

    public static final String testData = """      
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                                                       
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
                                           """;




    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(13);
        run(testInput, "400", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        var answer = "" + answer(input);
        showAnswer(answer, expectedOutput, startTime);
    }

    private static long answer(List<String> input){
        List<List<String>> reflections = new ArrayList<>();
        List<String> currentList = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            if(s.isBlank()){
                reflections.add(currentList);
                currentList = new ArrayList<>();
            } else {
                currentList.add(s);
            }
        }
        reflections.add(currentList);

        return reflections.stream().mapToLong(Thirteen::valueForReflection).sum();
    }

    private static long valueForReflection(List<String> reflection){
        long oldValue = calculateReflection(reflection);
        int height = reflection.size();
        int width = reflection.get(0).length();
        for (int i = 0; i < height; i++) {
            var s = reflection.get(i).toCharArray();
            for (int j = 0; j < width; j++) {
                s[j] = s[j] == '.' ? '#' : '.';
                var newString = new String(s);
                reflection.remove(i);
                reflection.add(i, newString);
                long newValue = calculateReflection(reflection);
                if(newValue > 0 && newValue != oldValue){
                    return newValue > oldValue ? newValue - oldValue : newValue;
                }
                s[j] = s[j] == '.' ? '#' : '.';
                newString = new String(s);
                reflection.remove(i);
                reflection.add(i, newString);
            }

        }
        System.out.println(oldValue);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(reflection.get(i).charAt(j));
            }
            System.out.print("\n");
        }
        throw new RuntimeException("No new value found!!!");
    }

    private static long calculateReflection(List<String> reflection){
        long v = 0;
        long h = 0;
        int height = reflection.size();
        for (int i = 0; i < height -1; i++) {
            for (int j = 1; true; j++) {
                int ti = i + j;
                int bi = i - (j - 1);
                if(ti >= height || bi < 0){
                    h += (i+1) * 100L;
                    break;
                }

                var t = reflection.get(ti);
                var b = reflection.get(bi);
                if(!t.equals(b)){
                    break;
                }
            }
        }
        int width = reflection.get(0).length();
        for (int i = 0; i < width -1; i++) {
            outerloop:
            for (int j = 1; true; j++)  {
                int ti = i + j;
                int bi = i - (j - 1);
                if(ti >= width || bi < 0){
                    h += (i+1);
                    break;
                }
                for (int k = 0; k < height; k++) {
                    var r = reflection.get(k);
                    var t = r.charAt(ti);
                    var b = r.charAt(bi);
                    if(t != b){
                        break outerloop;
                    }
                }

            }
        }
        return h + v;
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