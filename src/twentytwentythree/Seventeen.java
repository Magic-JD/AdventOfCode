package twentytwentythree;

import tools.Direction;
import tools.InternetParser;

import java.util.*;

import static tools.Direction.*;

public class Seventeen {

    public static final String testData = """   
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
                                           """;


    private static int width;
    private static int height;
    private static int[][] field;

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(17);
        run(testInput, "94", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    private record LavaHead(int i, int j, Direction d, int timesMoved, int score) {
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        width = input.get(0).length();
        long widthFactor = (long) Math.pow(10, String.valueOf(input.get(0).length()).length() * 2);
        height = input.size();
        field = new int[input.size()][input.get(0).length()];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = input.get(i).charAt(j) - '0';
            }
        }
        int minHeatLoss = Integer.MAX_VALUE;
        ArrayDeque<LavaHead> queue = new ArrayDeque<>();
        queue.add(new LavaHead(0, 1, E, 1,  field[height-1][width-1]));
        queue.add(new LavaHead(1, 0, S, 1, field[height-1][width-1]));
        Map<Integer, int[]> visited = new HashMap<>();
        Set<LavaHead> set = new HashSet<>();
        while (!queue.isEmpty()){
            LavaHead newest = queue.pop();
            if(newest.i == height-1 && newest.j == width-1){
                minHeatLoss = Math.min((newest.score), minHeatLoss);
            } else {
                int key = ((int)(newest.j * widthFactor)) + (newest.i * 10) + newest.timesMoved;
                int[] value = visited.getOrDefault(key, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE});
                int ord = newest.d.ordinal();
                int visitedScore = value[ord];
                if(newest.score < minHeatLoss && newest.score < visitedScore){
                    value[ord] = newest.score;
                    if(newest.timesMoved < 10){
                        continueStraight(newest).ifPresent(set::add);
                    }
                    if(newest.timesMoved > 3){
                        turnLeft(newest).ifPresent(set::add);
                        turnRight(newest).ifPresent(set::add);
                    }
                    visited.put(key, value);

                }
            }
            if(queue.isEmpty()){
                queue.addAll(set);
                set = new HashSet<>();
            }

        }
        //System.out.println(visited.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> e.getKey() + " " + Arrays.toString(e.getValue())).collect(Collectors.joining("\n")));
        var answer = "" + minHeatLoss;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static Optional<LavaHead> continueStraight(LavaHead c){
        Direction d = c.d;
        return switch (d){
            case N -> c.i > 0 ? Optional.of(new LavaHead(c.i-1, c.j, d, c.timesMoved + 1, c.score + field[c.i][c.j])) : Optional.empty();
            case E -> c.j < width-1 ? Optional.of(new LavaHead(c.i, c.j+1, d, c.timesMoved + 1, c.score + field[c.i][c.j])) : Optional.empty();
            case S -> c.i < height-1 ? Optional.of(new LavaHead(c.i+1, c.j, d, c.timesMoved + 1, c.score + field[c.i][c.j])) : Optional.empty();
            case W -> c.j > 0 ? Optional.of(new LavaHead(c.i, c.j-1, d, c.timesMoved + 1, c.score + field[c.i][c.j])) : Optional.empty();
        };
    }

    private static Optional<LavaHead> turnLeft(LavaHead c){
        Direction d = c.d;
        int ord = d.ordinal();
        Direction newDirection = values()[ord - 1 >= 0 ? ord - 1 : 3];
        return continueStraight(new LavaHead(c.i, c.j, newDirection, 0, c.score));
    }

    private static Optional<LavaHead> turnRight(LavaHead c){
        Direction d = c.d;
        int ord = d.ordinal();
        Direction newDirection = values()[ord + 1 <= 3 ? ord + 1 : 0];
        return continueStraight(new LavaHead(c.i, c.j, newDirection, 0, c.score));
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