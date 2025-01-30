package tasks;

import tools.Direction;
import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;

public class Sixteen {

    public static final String testData = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
                                    """;
    public static final int TURN_COST = 1000;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(16);
        run(testInput, "45", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);
        Set<Loc> values = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'S') {
                    values.add(new Loc(i, j, Direction.E));
                }
            }
        }
        ArrayDeque<Min> mins = new ArrayDeque<>();
        Loc l1 = values.stream().findAny().orElseThrow();
        Long minDistance = Long.MAX_VALUE;
        Set<Loc> locs = new HashSet<>();
        locs.add(l1);
        mins.add(new Min(l1, 0, Set.of(l1)));
        while (!mins.isEmpty()) {
            Min current = mins.pop();
            Loc pos = current.l;
            if (map[pos.i][pos.j] == 'E') {
                locs.addAll(current.path());
                minDistance = current.score;
                Long finalMinDistance1 = minDistance;
                mins = new ArrayDeque<>(mins.stream().filter(m -> m.score <= finalMinDistance1).sorted(Comparator.comparingInt(m -> Math.toIntExact(m.score))).toList());
            } else {
                int nextI = pos.i;
                int nextJ = pos.j;
                switch (pos.d) {
                    case N -> nextI--;
                    case E -> nextJ++;
                    case S -> nextI++;
                    case W -> nextJ--;
                }
                char nextC = map[nextI][nextJ];
                Loc l = new Loc(nextI, nextJ, pos.d);
                HashSet<Loc> path = new HashSet<>(current.path);
                path.add(l);
                mins.stream().filter(m -> m.score == current.score + 1 && m.l.equals(l)).forEach(m -> m.path.addAll(path));
                if (nextC != '#' && values.add(l)) {
                    mins.add(new Min(l, current.score + 1, path));
                }
                Loc left = new Loc(pos.i, pos.j, pos.d.turnLeft());
                mins.stream().filter(m -> m.score == current.score + TURN_COST && m.l.equals(left)).forEach(m -> m.path.addAll(current.path));
                if (values.add(left)) {
                    mins.add(new Min(left, current.score + TURN_COST, new HashSet<>(current.path)));
                }
                Loc right = new Loc(pos.i, pos.j, pos.d.turnRight());
                mins.stream().filter(m -> m.score == current.score + TURN_COST && m.l.equals(right) ).forEach(m -> m.path.addAll(current.path));
                if (values.add(right)) {
                    mins.add(new Min(right, current.score + TURN_COST, new HashSet<>(current.path())));
                }
                Long finalMinDistance = minDistance;
                mins = new ArrayDeque<>(mins.stream().filter(m -> m.score <= finalMinDistance).sorted(Comparator.comparingInt(m -> Math.toIntExact(m.score))).toList());
            }
        }
        showAnswer(String.valueOf(locs.stream().map(l -> new Locb(l.i, l.j)).collect(Collectors.toSet()).size()), expectedOutput, startTime);
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

    private record Loc(int i, int j, Direction d) {
    }
    private record Locb(int i, int j){}

    private record Min(Loc l, long score, Set<Loc> path) {
    }

}
