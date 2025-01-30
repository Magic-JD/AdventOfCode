package tasks;

import tools.Direction;
import tools.InternetParser;

import java.util.*;

public class Six {

    public static final String testData = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(6);
        run(testInput, "6", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Set<Loc> sum = new HashSet<>();
        char[][] array = input.stream().map(String::toCharArray).toArray(char[][]::new);
        int locI = 0;
        int locJ = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == '^') {
                    locI = i;
                    locJ = j;
                }
            }
        }
        Direction d = Direction.N;
        moveAround(d, array, locI, locJ, (i, j, direct) -> sum.add(new Loc(i, j)));
        long s = 0;
        for (Loc l : sum) {
            if(l.i == locI && l.j == locJ){
                continue;
            }
            array[l.i][l.j] = '#';
            Set<LocA> locs = new HashSet<>();
            try {
                moveAround(d, array, locI, locJ, (i, j, direct) -> {
                    if (!locs.add(new LocA(i, j, direct))) {
                        throw new LoopException();
                    }
                });
            } catch (LoopException e) {
                s++;
            }
            array[l.i][l.j] = '.';
        }
        showAnswer(String.valueOf(s), expectedOutput, startTime);
    }

    private static void moveAround(Direction d, char[][] array, int locI, int locJ, Handle function) {
        try {
            while (true) {
                switch (d) {
                    case N -> {
                        if (array[locI - 1][locJ] == '#') {
                            d = d.turnLeft();
                        } else {
                            locI--;
                            function.handle(locI, locJ, d);
                        }
                    }
                    case E -> {
                        if (array[locI][locJ + 1] == '#') {
                            d = d.turnLeft();
                        } else {
                            locJ++;
                            function.handle(locI, locJ, d);
                        }
                    }
                    case S -> {
                        if (array[locI + 1][locJ] == '#') {
                            d = d.turnLeft();
                        } else {
                            locI++;
                            function.handle(locI, locJ, d);
                        }
                    }
                    case W -> {
                        if (array[locI][locJ - 1] == '#') {
                            d = d.turnLeft();
                        } else {
                            locJ--;
                            function.handle(locI, locJ, d);
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private static List<Integer> reorderBook(List<Integer> book, Map<Integer, List<Integer>> orders) {
        List<Integer> found = new ArrayList<>();
        for (int i = 0; i < book.size(); i++) {
            int page = book.get(i);
            List<Integer> integers = orders.getOrDefault(page, new ArrayList<>());
            for (Integer after : integers) {
                int index = found.indexOf(after);
                if (index != -1) {
                    book.set(index, page);
                    book.set(i, after);
                    return reorderBook(book, orders);

                }
            }
            found.add(page);
        }
        return book;

    }

    private static boolean isValid(List<Integer> book, Map<Integer, List<Integer>> orders) {
        Set<Integer> found = new HashSet<>();
        for (Integer page : book) {
            List<Integer> integers = orders.getOrDefault(page, new ArrayList<>());
            for (Integer after : integers) {
                if (found.contains(after)) {
                    return false;
                }
            }
            found.add(page);
        }
        return true;
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

    private interface Handle {
        void handle(int i, int j, Direction d);
    }

    private record Loc(int i, int j) {
    }

    private record LocA(int i, int j, Direction d) {
    }

    private record Order(int before, int after) {
    }
}
