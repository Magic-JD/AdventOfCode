package tasks;

import tools.InternetParser;

import java.util.*;

public class Eight {

    public static final String testData = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(8);
        run(testInput, "34", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<Character, List<Loc>> mapping = new HashMap<>();
        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);
        int maxHeight = map.length;
        int maxWidth = map[0].length;
        for (int i = 0; i < maxHeight; i++) {
            for (int j = 0; j < maxWidth; j++) {
                char c = map[i][j];
                if(c != '.'){
                    List<Loc> locs = mapping.computeIfAbsent(c, dsfda -> new ArrayList<>());
                    locs.add(new Loc(i, j));
                }
            }
        }
        Set<Loc> an = new HashSet<>();
        mapping.values().forEach(lis -> {
            lis = lis.stream().sorted(Comparator.comparingInt(l -> l.i)).toList();
            for (int i = 0; i < lis.size()-1; i++) {
                for (int j = i+1; j < lis.size(); j++) {
                    Loc l1 = lis.get(i);
                    Loc l2 = lis.get(j);
                    an.add(l1);
                    an.add(l2);
                    int diffI = l2.i - l1.i;
                    boolean jAlsoSmaller = l1.j < l2.j;
                    int diffJ = jAlsoSmaller ? l2.j - l1.j : l1.j - l2.j;
                    int fi1 = l1.i;
                    int fj1 = l1.j;
                    int si2 = l2.i;
                    int sj2 = l2.j;
                    while((fi1 >= 0 && fi1 < maxHeight)|| (fj1 >= 0 && fj1 < maxWidth) || (si2 >= 0 && si2 < maxHeight) || (sj2 >= 0 && sj2 < maxWidth)){
                        fi1 -= diffI;
                        if(jAlsoSmaller) fj1 -= diffJ;
                        else fj1 += diffJ;
                        Loc loc1 = new Loc(fi1, fj1);
                        an.add(loc1);
                        si2 += diffI;
                        if(jAlsoSmaller) sj2 += diffJ;
                        else sj2 -= diffJ;
                        Loc loc2 = new Loc(si2, sj2);
                        an.add(loc2);
                    }
                }
            }
        });
        showAnswer(String.valueOf(an.stream().filter(l -> l.i >= 0 && l.i < maxHeight && l.j >= 0 && l.j < maxWidth).toList().size()), expectedOutput, startTime);
    }

    private record Loc(int i, int j){}



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
