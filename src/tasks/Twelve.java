package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Twelve {

    public static final String testData = """
AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA
""";



    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(12);
        run(testInput, "368", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = 0L;
        char[][] map = input.stream().map(String::trim).map(String::toCharArray).toArray(char[][]::new);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                char c = map[i][j];
                if(c != '.'){
                    sum += processMap(i, j, map);
                }
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static long processMap(int i, int j, char[][] map) {
        long p = 0;
        char c = map[i][j];

        Set<Loc> seen = new HashSet<>();
        List<Loc> around = new ArrayList<>();
        ArrayDeque<Loc> arrayDeque = new ArrayDeque<>();
        List<PerH> perH = new ArrayList<>();
        List<PerV> perV = new ArrayList<>();
        Loc current = new Loc(i, j, c);
        arrayDeque.add(current);
        seen.add(current);
        while (!arrayDeque.isEmpty()){
            Loc ex = arrayDeque.pop();
            int i1 = ex.i;
            int j1 = ex.j;
            try{
                Loc e = new Loc(i1 + 1, j1, map[i1 + 1][j1]);
                around.add(e);
                if(e.c != ex.c){
                    perH.add(new PerH((i1*2)+1, j1));
                }
            } catch (IndexOutOfBoundsException e){
                perH.add(new PerH((i1*2)+1, j1));
            }
            try{
                Loc e = new Loc(i1 - 1, j1, map[i1 - 1][j1]);
                around.add(e);
                if(e.c != ex.c){
                    perH.add(new PerH((i1*2)-1, j1));
                }
            } catch (IndexOutOfBoundsException e){
                perH.add(new PerH((i1*2)-1, j1));
            }
            try{
                Loc e = new Loc(i1, j1 + 1, map[i1][j1 + 1]);
                around.add(e);
                if(e.c != ex.c){
                    perV.add(new PerV(i1, (j1*2) + 1));
                }
            } catch (IndexOutOfBoundsException e){
                perV.add(new PerV(i1, (j1*2) + 1));
            }
            try{
                Loc e = new Loc(i1, j1 - 1, map[i1][j1 - 1]);
                around.add(e);
                if(e.c != ex.c){
                    perV.add(new PerV(i1, (j1*2) - 1));
                }
            } catch (IndexOutOfBoundsException e){
                perV.add(new PerV(i1, (j1*2) - 1));
            }
            arrayDeque.addAll(around.stream().filter(l -> l.c == ex.c).filter(seen::add).toList());
            around = new ArrayList<>();
        }
        seen.forEach(l -> map[l.i][l.j] = '.');
        return (long) seen.size() * calculateSides(perH, perV);
    }

    private record PerV(int i, int j){}
    private record PerH(int i, int j){}

    private static int calculateSides(List<PerH> perHs, List<PerV> perVs) {
        Map<Integer, List<PerH>> tops = perHs.stream().collect(Collectors.groupingBy(l -> l.i));
        Map<Integer, List<PerV>> bottoms = perVs.stream().collect(Collectors.groupingBy(l -> l.j));
        AtomicInteger perimeter = new AtomicInteger(tops.size() + bottoms.size());
        tops.forEach((in, locs) -> {
            List<PerH> list = locs.stream().sorted(Comparator.comparingInt(l -> l.j)).toList();
            int start = list.getFirst().j();
            for (int i = 1; i < list.size(); i++) {
                int currentJ = list.get(i).j;
                if(currentJ - start == 1 && bottoms.entrySet().stream().noneMatch(ent -> ent.getKey() == currentJ + (currentJ-1) && ent.getValue().stream().anyMatch(l -> in/2 == l.i))){
                    start = currentJ;
                } else {
                    start = currentJ;
                    perimeter.addAndGet(1);
                }
            }

        });
        bottoms.forEach((in, locs) -> {
            List<PerV> list = locs.stream().sorted(Comparator.comparingInt(l -> l.i)).toList();
            int start = list.getFirst().i();
            for (int i = 1; i < list.size(); i++) {
                int currentI = list.get(i).i;
                if(currentI - start == 1 && tops.entrySet().stream().noneMatch(ent -> ent.getKey() == currentI + (currentI-1) && ent.getValue().stream().anyMatch(l -> in/2 == l.j))){
                    start = currentI;
                } else {
                    start = currentI;
                    perimeter.addAndGet(1);
                }
            }

        });
        return perimeter.get();
    }

    private record Loc(int i, int j, char c){}


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
