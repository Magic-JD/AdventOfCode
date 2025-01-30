package tasks;

import tools.InternetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Fifteen {

    public static final String testData = """
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                        """;


    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(15);
        run(testInput, "10092", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<String> mapList = new ArrayList<>();
        List<String> instructionsList = new ArrayList<>();
        boolean isMap = true;
        for (String s : input) {
            if (isMap) {
                if (s.isBlank()) {
                    isMap = false;
                } else {
                    mapList.add(s);
                }
            } else {
                instructionsList.add(s);
            }
        }
        char[] instructions = String.join("", instructionsList).toCharArray();
        char[][] map = mapList.stream().map(s -> s.chars().mapToObj(c -> switch (c){
            case '#' -> "##";
            case '.' -> "..";
            case 'O' -> "[]";
            case '@' -> "@.";
            default -> throw new UnsupportedOperationException();
        }).collect(Collectors.joining()).toCharArray()).toArray(char[][]::new);
        int il = 0;
        int jl = 0;
        for (int i = 0; i <map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == '@'){
                    il = i;
                    jl = j;
                }
            }
        }
        for (char c : instructions) {
            if (canMove(il, jl, map, c)) {
                move(il, jl, map, c);
                switch (c) {
                    case '^' -> il--;
                    case '>' -> jl++;
                    case 'v' -> il++;
                    case '<' -> jl--;
                    default -> throw new UnsupportedOperationException();
                }
            }
        }
        long sum = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == '['){
                    sum += (100L * i) + j;
                }
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static void move(int i, int j, char[][] map, char c) {
        char currentMoving = map[i][j];
        int nextI = i;
        int nextJ = j;
        switch (c){
            case '^' -> nextI--;
            case '>' -> nextJ++;
            case 'v' -> nextI++;
            case '<' -> nextJ--;
            default -> throw new UnsupportedOperationException();
        }
        char next = map[nextI][nextJ];
        if(next == '#'){
            throw new UnsupportedOperationException();
        }
        if(next == '.'){
            map[i][j] = '.';
            map[nextI][nextJ] = currentMoving;
            return;
        }
        if((c == 'v' || c == '^')){
            if(next == '['){
                move(nextI, nextJ, map, c);
                move(nextI, nextJ+1, map, c);
                map[i][j] = '.';
                map[nextI][nextJ] = currentMoving;
                return;
            }
            if(next == ']'){
                move(nextI, nextJ, map, c);
                move(nextI, nextJ-1, map, c);
                map[i][j] = '.';
                map[nextI][nextJ] = currentMoving;
                return;
            }
            throw new UnsupportedOperationException();
        }
        move(nextI, nextJ, map, c);
        map[i][j] = '.';
        map[nextI][nextJ] = currentMoving;
    }

    private static boolean canMove(int i, int j, char[][] map, char c) {
        int nextI = i;
        int nextJ = j;
        switch (c){
            case '^' -> nextI--;
            case '>' -> nextJ++;
            case 'v' -> nextI++;
            case '<' -> nextJ--;
            default -> throw new UnsupportedOperationException();
        }
        char next = map[nextI][nextJ];
        if(next == '#'){
            return false;
        }
        if(next == '.'){
            return true;
        }
        if((c == 'v' || c == '^')){
            if(next == '['){
                return canMove(nextI, nextJ, map, c) && canMove(nextI, nextJ+1, map, c);
            }
            if(next == ']'){
                return canMove(nextI, nextJ, map, c) && canMove(nextI, nextJ-1, map, c);
            }
            throw new UnsupportedOperationException();
        }
        return canMove(nextI, nextJ, map, c);
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
