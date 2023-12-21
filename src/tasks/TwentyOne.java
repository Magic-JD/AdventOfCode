package tasks;

import tools.InternetParser;

import java.util.*;

public class TwentyOne {

    public static final String testData = """      
.................................
.....###.#......###.#......###.#.
.###.##..#..###.##..#..###.##..#.
..#.#...#....#.#...#....#.#...#..
....#.#........#.#........#.#....
.##...####..##...####..##...####.
.##..#...#..##..#...#..##..#...#.
.......##.........##.........##..
.##.#.####..##.#.####..##.#.####.
.##..##.##..##..##.##..##..##.##.
.................................
.................................
.....###.#......###.#......###.#.
.###.##..#..###.##..#..###.##..#.
..#.#...#....#.#...#....#.#...#..
....#.#........#.#........#.#....
.##...####..##..S####..##...####.
.##..#...#..##..#...#..##..#...#.
.......##.........##.........##..
.##.#.####..##.#.####..##.#.####.
.##..##.##..##..##.##..##..##.##.
.................................
.................................
.....###.#......###.#......###.#.
.###.##..#..###.##..#..###.##..#.
..#.#...#....#.#...#....#.#...#..
....#.#........#.#........#.#....
.##...####..##...####..##...####.
.##..#...#..##..#...#..##..#...#.
.......##.........##.........##..
.##.#.####..##.#.####..##.#.####.
.##..##.##..##..##.##..##..##.##.
.................................
                              """;


    private static int width = 0;
    private static int height = 0;

    //After 66 we have a perfect diamond
    //Then after 132 more it happens again

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(21);
        //run(testInput, "-", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long numberOfSteps = 26501365;
        long initialTimeFrame = 66;
        long secondaryTimeFram = 132;
        long remainingAfterFirstDiamondReached = numberOfSteps - initialTimeFrame;
        long timesDiamondExpands = remainingAfterFirstDiamondReached / secondaryTimeFram;
        long timesNeededAfter = remainingAfterFirstDiamondReached % secondaryTimeFram;
        height = input.size();
        width = input.get(0).length();
        Plot[][] map = new Plot[height][width];
        long[][] countPerLoc = new long[height][width];
        Step firstLocation = null;
        for (int i = 0; i < height; i++) {
            String s = input.get(i);
            for (int j = 0; j < width; j++) {
                char c = s.charAt(j);
                Plot plot;
                if(c == '#'){
                    plot = new Plot(false, i, j);
                } else if (c == '.'){
                    plot = new Plot(true, i, j);
                } else {
                    plot = new Plot(true, i, j);
                    firstLocation = new Step(i, j, 0, 0);
                }
                map[i][j] = plot;
            }
        }
        long last = 0;

        long countEven = 0;
        long countOdd = 0;
        Set<Step> lastSteps = new HashSet<>();
        Set<Step> currentSteps = new HashSet<>();
        ArrayDeque<Step> steps = new ArrayDeque<>();
        steps.add(firstLocation);
        currentSteps.add(firstLocation);
        int i;
        for (i = 0; i < numberOfSteps; i++) {
            Set<Step> newToCheck = new HashSet<>();
            System.out.println(i);
            printArr(map, steps);
            while (!steps.isEmpty()){
                Step step = steps.pop();
               if(step.i > 0 && map[step.i-1][step.j].stepable){
                   Step s = new Step(step.i - 1, step.j, step.metaI, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.i < height-1 && map[step.i+1][step.j].stepable){
                   Step s = new Step(step.i + 1, step.j, step.metaI, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.j > 0 && map[step.i][step.j-1].stepable){
                   Step s = new Step(step.i, step.j - 1, step.metaI, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.j < width -1 && map[step.i][step.j+1].stepable){
                   Step s = new Step(step.i, step.j + 1, step.metaI, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }

               if(step.i == 0 && map[height-1][step.j].stepable){
                   Step s = new Step(height - 1, step.j, step.metaI - 1, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.i == height-1 && map[0][step.j].stepable){
                   Step s = new Step(0, step.j, step.metaI + 1, step.metaJ);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.j == 0 && map[step.i][height-1].stepable){
                   Step s = new Step(step.i, height - 1, step.metaI, step.metaJ - 1);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
               if(step.j == width -1 && map[step.i][0].stepable){
                   Step s = new Step(step.i, 0, step.metaI, step.metaJ + 1);
                   if(!lastSteps.contains(s)){
                       newToCheck.add(s);
                   }
               }
            }
            if(i %2 == 0){
                countEven += lastSteps.size();
            } else {
                countOdd += lastSteps.size();
            //    System.out.println(newToCheck.size());
            }
            lastSteps = currentSteps;
            currentSteps = newToCheck;
            steps = new ArrayDeque<>(newToCheck);
        }


        var answer = "" + (steps.size() + (i%2 != 0 ? countEven : countOdd));
        showAnswer(answer, expectedOutput, startTime);
    }

    private record Plot(boolean stepable, int i, int j) {
    }

    private record Step(int i, int j, int metaI, int metaJ){}

    private static void printArr(Plot[][] map, Collection<Step> steppingOn){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int finalI = i;
                int finalJ = j;
                if(steppingOn.stream().anyMatch(step -> step.i == finalI && step.j == finalJ)){
                    stringBuilder.append(ANSI_RED + "0" + ANSI_RESET);
                } else {
                    stringBuilder.append(map[i][j].stepable ? "." : "#");
                }
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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