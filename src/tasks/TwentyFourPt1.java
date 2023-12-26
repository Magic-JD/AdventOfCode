package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.util.Arrays;
import java.util.List;

public class TwentyFourPt1 {

    public static final String testData = """      
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
                                                       """;


    /**
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(24);
        run(testInput, "2", System.currentTimeMillis(), 7, 27);
        run(mainInput, "???", System.currentTimeMillis(), 200_000_000_000_000L, 400_000_000_000_000L);
    }

    public static void run(List<String> input, String expectedOutput, long startTime, long min, long max) {
        List<HailInit> storm = input.stream().map(TwentyFourPt1::parseHailFromString).toList();

        List<Line2D> lines = storm.stream().map(h -> onvertHailToLines(min, max, h)).toList();
        double collisionCount = 0;
        for (Line2D hailstoneA : lines) {
            for (Line2D hailstoneB : lines) {
                if (!hailstoneA.equals(hailstoneB)) {
                    double ma = hailstoneA.equation.m;
                    double mb = hailstoneB.equation.m;
                    if (ma != mb) {
                        double ca = hailstoneA.equation.c;
                        double cb = hailstoneB.equation.c;
                        double newM = ma - mb;
                        double newB = cb - ca;
                        double x = newB / newM;
                        double y = (ma * x) + ca;
                        double aminX = Math.min(hailstoneA.startX, hailstoneA.endX);
                        double amaxX = Math.max(hailstoneA.startX, hailstoneA.endX);
                        double bminX = Math.min(hailstoneB.startX, hailstoneB.endX);
                        double bmaxX = Math.max(hailstoneB.startX, hailstoneB.endX);
                        if (x > Math.max(aminX, bminX)
                                && x < Math.min(amaxX, bmaxX)
                        ) {
                            if (x > max || x < min || y > max || y < min) {
                                double dMin = min;
                                double dMax = max;
                                //throw new RuntimeException();
                            } else {
                                collisionCount++;
                            }
                        }
                    }
                }
            }
        }
        var answer = "" + (long) (collisionCount / 2);
        showAnswer(answer, expectedOutput, startTime);
    }

    private static Line2D onvertHailToLines(long min, long max, HailInit h) {
        double startX = h.px;
        double startY = h.py;
        double distanceToXEdge = h.vx > 0 ? max - startX : startX - min;
        double distanceToYEdge = h.vy > 0 ? max - startY : startY - min;
        double nsToReachXEdge = distanceToXEdge / Math.abs(h.vx);
        double nsToReachYEdge = distanceToYEdge / Math.abs(h.vy);
        double nsTravelled = Math.min(nsToReachXEdge, nsToReachYEdge);
        double endX = startX + (h.vx * nsTravelled);
        double endY = startY + (h.vy * nsTravelled);
        Equation e = calculateEquation(startX, endX, startY, endY);
        return new Line2D(startX, startY, endX, endY, e);
    }

    private static Equation calculateEquation(double startX, double endX, double startY, double endY) {
        double m = (startY - endY) / (startX - endX);
        double ex = m * startX;
        double c = startY - ex;
        return new Equation(m, c);
    }

    private static HailInit parseHailFromString(String s) {
        s = s.replaceAll("@", ",");
        long[] ns = Arrays.stream(LineUtils.split(s, ",")).mapToLong(Long::parseLong).toArray();
        return new HailInit(ns[0], ns[1], ns[2], ns[3], ns[4], ns[5]);
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

    private record HailInit(double px, double py, double pz, double vx, double vy, double vz) {
    }

    private record Hail(Line2D xz, Line2D yz) {
    }

    private record Line2D(double startX, double startY, double endX, double endY, Equation equation) {
    }

    private record Equation(double m, double c) {
    }
}