package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Twenty {

    public static final String testData = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(20);
        run(testInput, "29", System.currentTimeMillis(), 72);
        run(mainInput, "???", System.currentTimeMillis(), 100);
    }

    public static void run(List<String> input, String expectedOutput, long startTime, int timeSaved) {
        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);
        Map<Coord, Integer> locationToDistance = new HashMap<>();
        List<Coord> coords = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'S') {
                    coords.add(new Coord(i, j));
                }
            }
        }
        int pureLength = getLength(coords, map, Integer.MAX_VALUE, locationToDistance, new HashSet<>());
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '.') {
                    Coord key = new Coord(i, j);
                    locationToDistance.put(key, getLength(List.of(key), map, Integer.MAX_VALUE, locationToDistance, new HashSet<>()));
                }
                if (map[i][j] == 'E') {
                    locationToDistance.put(new Coord(i, j), 0);
                }
            }
        }
        int maxLength = pureLength - timeSaved;
        long cheats = findNumberOfCheats(coords, map, maxLength, new HashSet<>(coords), locationToDistance);
        showAnswer(String.valueOf(cheats), expectedOutput, startTime);
    }

    private static long findNumberOfCheats(List<Coord> coords, char[][] map, int maxLength, Set<Coord> visited, Map<Coord, Integer> maxDistances) {
        long total = 0;
        while (maxLength >= 0 && !coords.isEmpty()) {
            List<Coord> next = new ArrayList<>();
            int finalMaxLength = maxLength;
            for (Coord start : coords) {
                total += IntStream.range(2, Math.min(maxLength+1, 21))
                        .mapToLong(i -> coordsForI(i, start, finalMaxLength, map, maxDistances)).sum();
                List<Coord> list = Stream.of(
                                new Coord(start.i + 1, start.j),
                                new Coord(start.i - 1, start.j),
                                new Coord(start.i, start.j + 1),
                                new Coord(start.i, start.j - 1)
                        )
                        .filter(coord -> coord.i >= 0 && coord.j >= 0 && coord.i < map.length && coord.j < map[0].length)
                        .filter(coord -> map[coord.i][coord.j] == '.')
                        .filter(visited::add).toList();
                next.addAll(list);
            }
            maxLength--;
            coords = next;

        }
        return total;
    }

    private static long coordsForI(int i, Coord start, int finalMaxLength, char[][] map, Map<Coord, Integer> maxDistances) {
        Set<Coord> coords = new HashSet<>();
        for (int j = 0; j <= i; j++) {
            coords.add(new Coord(start.i + j, start.j + (i - j)));
            coords.add(new Coord(start.i - j, start.j + (i - j)));
            coords.add(new Coord(start.i + j, start.j - (i - j)));
            coords.add(new Coord(start.i - j, start.j - (i - j)));
        }
        return coords.stream().filter(coord -> coord.i >= 0 && coord.j >= 0 && coord.i < map.length && coord.j < map[0].length)
                .filter(coord -> map[coord.i][coord.j] == '.' || map[coord.i][coord.j] == 'E')
                .filter(coord -> maxDistances.get(coord) <= finalMaxLength - i).count();
    }

    private static int getLength(List<Coord> coords, char[][] map, int max, Map<Coord, Integer> distances, Set<Coord> visited) {
        if (distances.containsKey(coords.getFirst())) {
            return distances.get(coords.getFirst());
        }
        int pureLength = 1;
        Coord first = coords.getFirst();
        visited.add(first);
        if (map[first.i][first.j] == 'E') {
            return 0;
        }
        while (!coords.isEmpty()) {
            List<Coord> next = new ArrayList<>();
            for (Coord coord : coords) {
                try {
                    int i = coord.i + 1;
                    int j = coord.j;
                    if (visited.add(new Coord(i, j))) {
                        char nc = map[i][j];
                        if (nc == 'E') {
                            return pureLength;
                        }
                        if (nc == '.') {
                            next.add(new Coord(i, j));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            for (Coord coord : coords) {
                try {
                    int i = coord.i - 1;
                    int j = coord.j;
                    if (visited.add(new Coord(i, j))) {
                        char nc = map[i][j];
                        if (nc == 'E') {
                            return pureLength;
                        }
                        if (nc == '.') {
                            next.add(new Coord(i, j));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            for (Coord coord : coords) {
                try {
                    int i = coord.i;
                    int j = coord.j + 1;
                    if (visited.add(new Coord(i, j))) {

                        char nc = map[i][j];
                        if (nc == 'E') {
                            return pureLength;
                        }
                        if (nc == '.') {
                            next.add(new Coord(i, j));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            for (Coord coord : coords) {
                try {
                    int i = coord.i;
                    int j = coord.j - 1;
                    if (visited.add(new Coord(i, j))) {
                        char nc = map[i][j];
                        if (nc == 'E') {
                            return pureLength;
                        }
                        if (nc == '.') {
                            next.add(new Coord(i, j));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            pureLength++;
            if (pureLength > max) {
                return Integer.MAX_VALUE;
            }
            coords = next;
        }
        return Integer.MAX_VALUE;
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

    private record Coord(int i, int j) {
    }
}
