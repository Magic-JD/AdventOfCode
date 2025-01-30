package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;

public class Eighteen {

    public static final String testData = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(18);
        run(testInput, "6,1", System.currentTimeMillis(), 6);
        run(mainInput, "???", System.currentTimeMillis(), 70);
    }

    public static void run(List<String> input, String expectedOutput, long startTime, int dimension) {
        List<Coord> collect = input.stream()
                .map(s -> s.split(","))
                .map(s -> new Coord(Integer.parseInt(s[0]), Integer.parseInt(s[1])))
                .toList();
        boolean[][] map = new boolean[dimension+1][dimension+1];
        printMap(map);
        for (int i = 0; i <= dimension; i++) {
            for (int j = 0; j <= dimension; j++) {
                map[i][j] = true;
            }
        }
        printMap(map);
        Coord finalC = null;
        for (int i = 0; i < collect.size(); i++) {
            Coord current = collect.get(i);
            map[current.i][current.j] = false;
            try {
                getCount(List.of(new Coord(0, 0)), Arrays.stream(map).map(arr -> Arrays.copyOf(arr, dimension+1)).toArray(boolean[][]::new), dimension);
            } catch (RuntimeException e){
                finalC = current;
                break;
            }
        }
        showAnswer(finalC.i + "," + finalC.j, expectedOutput, startTime);
    }

    private static int getCount(List<Coord> coords, boolean[][] map, int dimention) {
        int count = 1;
        Coord end = new Coord(dimention, dimention);
        while (!coords.isEmpty()){
            List<Coord> newFound = new ArrayList<>();
            for (Coord coord : coords) {
                try {
                    int i = coord.i + 1;
                    int j = coord.j;
                    if(map[i][j]){
                        map[i][j] = false;
                        newFound.add(new Coord(i, j));
                    }
                } catch (IndexOutOfBoundsException e){}
                try {
                    int i = coord.i - 1;
                    int j = coord.j;
                    if(map[i][j]){
                        map[i][j] = false;
                        newFound.add(new Coord(i, j));
                    }
                } catch (IndexOutOfBoundsException e){}
                try {
                    int i = coord.i;
                    int j = coord.j + 1;
                    if(map[i][j]){
                        map[i][j] = false;
                        newFound.add(new Coord(i, j));
                    }
                } catch (IndexOutOfBoundsException e){}
                try {
                    int i = coord.i;
                    int j = coord.j - 1;
                    if(map[i][j]){
                        map[i][j] = false;
                        newFound.add(new Coord(i, j));
                    }
                } catch (IndexOutOfBoundsException e){}
            }
            if(newFound.contains(end)) return count;
            count++;
            coords = newFound;
        }
        throw new RuntimeException();
    }

    private static void printMap(boolean[][] map){
//        for (boolean[] booleans : map) {
//            for (int j = 0; j < map.length; j++) {
//                System.out.print(booleans[j] ? "." : "#");
//            }
//            System.out.println();
//        }
//        System.out.println("------------");
    }


    private record Coord(int i, int j){}

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
