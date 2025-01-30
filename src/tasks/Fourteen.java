package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Fourteen {

    public static final String testData = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """;
    static List<Robot> startingRobots = new ArrayList<>();

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(14);
        //run(testInput, "12", System.currentTimeMillis(), 7, 11);
        run(mainInput, "???", System.currentTimeMillis(), 103, 101);
    }

    public static void run(List<String> input, String expectedOutput, long startTime, int height, int width) {
        List<Robot> robots = input.stream().map(s -> {
            String[] split = s.split(" ");
            String position = split[0].split("=")[1];
            String velocity = split[1].split("=")[1];
            String[] spltp = position.split(",");
            int px = Integer.parseInt(spltp[0]);
            int py = Integer.parseInt(spltp[1]);
            String[] splitv = velocity.split(",");
            int vx = Integer.parseInt(splitv[0]);
            int vy = Integer.parseInt(splitv[1]);
            return new Robot(px, py, vx, vy);

        }).toList();
        startingRobots = robots;
        for (int i = 0; i <= 10000; i++) {
            robots = robots.stream().map(r -> {
                int xpos = (r.xpos + r.vx);
                if(xpos < 0){
                    xpos += width;
                }
                xpos %= width;
                int ypos = (r.ypos + r.vy);
                if(ypos < 0){
                    ypos += height;
                }
                ypos %= height;
                return new Robot(xpos, ypos, r.vx, r.vy);
            }).toList();
            if(isTree(robots, height, width, 0)){
                for (int j = 0; j < height; j++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int k = 0; k < width; k++) {
                        int finalJ = j;
                        int finalK = k;
                        if(robots.stream().anyMatch(r -> r.ypos == finalJ && r.xpos == finalK)){
                            stringBuilder.append('*');
                        } else {
                            stringBuilder.append(".");
                        }
                    }
                    System.out.println(stringBuilder);

                }
                System.out.println("--- " + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //showAnswer(String.valueOf(i), expectedOutput, startTime);
                //return;
            }

        }
        long sum = 1;
        long topLeft = robots.stream().filter(r -> r.xpos < width / 2 && r.ypos < height / 2).count();
        long topRight = robots.stream().filter(r -> r.xpos > width / 2 && r.ypos < height / 2).count();
        long bottomRight = robots.stream().filter(r -> r.xpos < width / 2 && r.ypos > height / 2).count();
        long bottomLeft = robots.stream().filter(r -> r.xpos > width / 2 && r.ypos > height / 2).count();
        sum *= topLeft;
        sum *= topRight;
        sum *= bottomRight;
        sum *= bottomLeft;
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private record Pos(int x, int y){}

    private static boolean isTree(List<Robot> robots, int height, int width, int offset) {
        return robots.stream().map(r -> new Pos(r.xpos, r.ypos)).collect(Collectors.toSet()).size() > 495;
        //return startingRobots.containsAll(robots) && robots.containsAll(startingRobots);
       // return robots.stream().anyMatch(r -> r.ypos == height-1 && r.xpos == width/2) && robots.stream().anyMatch(r -> r.ypos == 0 && r.xpos == width/2);
//        Map<Integer, List<Robot>> collect = robots.stream().collect(groupingBy(r -> r.ypos));
//        boolean value = collect.values().stream().anyMatch(l -> l.size() >= 7);
//        return value;
//        for (int i = offset; i < height - (2-offset); i++) {
//            int finalI = i;
//            if(robots.stream().noneMatch(r -> r.ypos == finalI && r.xpos == width / 2 + (finalI - offset))){
//                return false;
//            }
//            if(robots.stream().noneMatch(r -> r.ypos == finalI && r.xpos == width / 2 - (finalI - offset))){
//                return false;
//            }
//
//        }
//        return true;
    }

    private record Robot(int xpos, int ypos, int vx, int vy){}

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
