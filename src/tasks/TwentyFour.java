package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class TwentyFour {

    public static final String testData = """      
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            19, 13, 30 @ -2,  1, -2
            20, 19, 15 @  1, -5, -3
            18, 19, 22 @ -1, -1, -2
                                                       """;
    public static final int SCALE = 64;


    /**
     * You can solve it without making that assumption. Translate into the frame of reference of the first hailstone,
     * in other words, subtract the position and velocity of the first hailstone from all of them. So in this frame,
     * the rock must go through the origin at some time.
     * <p>
     * The path of the second hailstorm forms a line. The rock must also intersect this line.
     * That means, the rock must be in the plane formed by that line and the origin.
     * <p>
     * Find when and where hailstones 3 & 4 intersect the plane. That gives the position and velocity of the rock at
     * two different times, which determines the entire trajectory of the rock.
     * <p>
     * So you only need 4 hailstones to determine the answer.
     * <p>
     * Ok so I took the advice for how to solve this one from reddit. Code is mine but derived from this. I am a programmer,
     * not a mathematician :L
     * <p>
     * https://www.youtube.com/watch?v=rL9UXzZYYo4 //Find a plane from 3 points
     * <p>
     * https://www.youtube.com/watch?v=_W3aVWsMp14 // Find the point of interesection given a line.
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(24);
        run(testInput, "47", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        HailInit[] storm = input.stream().map(TwentyFour::parseHailFromString).limit(4).toArray(HailInit[]::new);
        HailInit relationalHail = storm[0];
        storm = Arrays.stream(storm).map(h -> new HailInit(h.px.subtract(relationalHail.px), h.py.subtract(relationalHail.py), h.pz.subtract(relationalHail.pz), h.vx.subtract(relationalHail.vx), h.vy.subtract(relationalHail.vy), h.vz.subtract(relationalHail.vz))).toArray(HailInit[]::new);
        //Find normal vector
        //Pass relative 0 through normal vector
        HailInit usedForPlaneLine = storm[1];
        Plane n = getPlane(new Point3D(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), usedForPlaneLine);

        HailInit planeIntersectPoint = storm[2];
        HailInit planeIntersectPoint2 = storm[3];
        Point3D firstIntersection = intersectionPoint(n, planeIntersectPoint);
        Point3D secondIntersection = intersectionPoint(n, planeIntersectPoint2);

        BigDecimal timeNsFirst = (planeIntersectPoint.py.subtract(firstIntersection.y)).divide(planeIntersectPoint.vy, SCALE, RoundingMode.HALF_DOWN).abs();
        BigDecimal timeNsSecond = (planeIntersectPoint2.py.subtract(secondIntersection.y)).divide(planeIntersectPoint2.vy, SCALE, RoundingMode.HALF_DOWN).abs();
        BigDecimal timeBetween = (timeNsFirst.subtract(timeNsSecond)).abs();
        boolean firstEarliest = timeNsFirst.compareTo(timeNsSecond) < 0;
        BigDecimal distanceX = firstEarliest ? secondIntersection.x.subtract(firstIntersection.x) : firstIntersection.x.subtract(secondIntersection.x);
        BigDecimal distanceY = firstEarliest ? secondIntersection.y.subtract(firstIntersection.y) : firstIntersection.y.subtract(secondIntersection.y);
        BigDecimal distanceZ = firstEarliest ? secondIntersection.z.subtract(firstIntersection.z) : firstIntersection.z.subtract(secondIntersection.z);
        BigDecimal vx = (distanceX.divide(timeBetween, SCALE, RoundingMode.HALF_DOWN));
        BigDecimal vy = (distanceY.divide(timeBetween, SCALE, RoundingMode.HALF_DOWN));
        BigDecimal vz = (distanceZ.divide(timeBetween, SCALE, RoundingMode.HALF_DOWN));

        BigDecimal px = (firstIntersection.x.subtract(vx.multiply(timeNsFirst))).add(relationalHail.px);
        BigDecimal py = (firstIntersection.y.subtract(vy.multiply(timeNsFirst))).add(relationalHail.py);
        BigDecimal pz = (firstIntersection.z.subtract(vz.multiply(timeNsFirst))).add(relationalHail.pz);
        showAnswer("" + px.add(py).add(pz).toBigInteger(), expectedOutput, startTime);
    }

    private static Plane getPlane(Point3D p, HailInit usedForPlaneLine) {
        Point3D q = new Point3D(usedForPlaneLine.px, usedForPlaneLine.py, usedForPlaneLine.pz);
        Point3D r = new Point3D(q.x.subtract(usedForPlaneLine.vx), q.y.subtract(usedForPlaneLine.vy), q.z.subtract(usedForPlaneLine.vz));
        Vector a = betweenPoints(p, q);
        Vector b = betweenPoints(p, r);
        Vector n = crossProduct(a, b);
        return new Plane(n.a.negate(), n.b.negate(), n.c.negate(), ((n.a.multiply(p.x.negate())).add(n.b.multiply(p.y.negate())).add(n.c.multiply(p.z.negate()))));
    }

    private static Point3D intersectionPoint(Plane p, HailInit initialStart) {
        // due to using point 0, 0, 0 the vector = 0. Therefore we can sum x0 values and divide by the a values
        BigDecimal a = initialStart.vx;
        BigDecimal b = initialStart.vy;
        BigDecimal c = initialStart.vz;
        BigDecimal x0 = initialStart.px;
        BigDecimal y0 = initialStart.py;
        BigDecimal z0 = initialStart.pz;

        BigDecimal t = (p.equality.subtract((x0.multiply(p.a)).add(y0.multiply(p.b)).add(z0.multiply(p.c)))).divide((a.multiply(p.a)).add(b.multiply(p.b)).add(c.multiply(p.c)), SCALE, RoundingMode.HALF_DOWN);
        return new Point3D(x0.add(a.multiply(t)), y0.add(b.multiply(t)), z0.add(c.multiply(t)));
    }

    private static Vector crossProduct(Vector a, Vector b) {
        BigDecimal[][] matrix = new BigDecimal[][]{{a.a, a.b, a.c}, {b.a, b.b, b.c}};
        BigDecimal iE = matrix[0][1].multiply(matrix[1][2]).subtract(matrix[0][2].multiply(matrix[1][1]));
        BigDecimal jE = matrix[0][0].multiply(matrix[1][2]).subtract(matrix[0][2].multiply(matrix[1][0]));
        BigDecimal kE = matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        return new Vector(iE, jE.negate(), kE);
    }

    private static Vector betweenPoints(Point3D a, Point3D b) {
        return new Vector(a.x.subtract(b.x), a.y.subtract(b.y), a.z.subtract(b.z));

    }

    private static HailInit parseHailFromString(String s) {
        s = s.replaceAll("@", ",");
        BigDecimal[] ns = Arrays.stream(LineUtils.split(s, ",")).map(i -> BigDecimal.valueOf(Long.parseLong(i))).toArray(BigDecimal[]::new);
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

    private record HailInit(BigDecimal px, BigDecimal py, BigDecimal pz, BigDecimal vx, BigDecimal vy, BigDecimal vz) {
    }

    private record Point3D(BigDecimal x, BigDecimal y, BigDecimal z) {
    }

    private record Vector(BigDecimal a, BigDecimal b, BigDecimal c) {
    }

    private record Plane(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal equality) {
    }

}