package tasks;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fifteen {

    public static final String testData = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
                                           """;


    private static final List<Lens>[] hashMap = new List[256];
    private record Lens(String label, int focus){}


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(15);
        run(testInput, "145", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        for (int i = 0; i < 256; i++) {
            hashMap[i] = new ArrayList<>();
        }
        var answer = "" + answer(input);
        showAnswer(answer, expectedOutput, startTime);
    }

    private static long answer(List<String> input) {
        List<String> list = input.stream().flatMap(i ->
                Arrays.stream(i.split(","))).toList();
        for(String s : list){
            String[] instr = s.split("=");
            String lable = instr[0].replace("-", "");
            int currentValue = 0;
            char[] charArray = lable.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                currentValue = ((currentValue + charArray[i]) * 17) % 256;
            }

            List<Lens> all = hashMap[currentValue];
            if(instr.length == 2){
                Lens newLens = new Lens(lable, Integer.parseInt(instr[1]));
                Optional<Lens> old = all.stream().filter(l ->
                        l.label.equals(newLens.label)).findFirst();
                if(old.isPresent()){
                    int index = all.indexOf(old.get());
                    all.remove(index);
                    all.add(index, newLens);
                } else {
                    all.add(all.size(), newLens);
                }
            } else {
                all.stream()
                        .filter(l -> l.label.equals(lable))
                        .findFirst()
                        .ifPresent(all::remove);
            }
        }
        long sum = 0;
        for (int i = 0; i < hashMap.length; i++) {
            List<Lens> lens = hashMap[i];
            for (int j = 0; j < lens.size(); j++) {
                long key = (long) (i + 1) * (j + 1) * lens.get(j).focus;
                sum += key;
            }
        }
        return sum;
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