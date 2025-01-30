package tasks;

import tools.InternetParser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Thirteen {

    public static final String testData = """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
""";

    // Button A: X+17, Y+86
    // Button B: X+84, Y+37
    // Prize: X=7870, Y=6450
    //
    // Button a is pressed x times (min val)
    // Button b is pressed y times
    //
    // 17x + 84y = 7870
    // 86x + 37y = 6450
    //
    // -> Can add these
    //
    // 103x + 113y = 14320
    //
    // Is there a way to minus?
    //
    // 84y = 7870 - 17x
    // 37y = 6450 - 86x
    //
    // 47y = 69x + 1420
    //
    // 47y - 69x = 1420
    //
    // Move the x to the right


    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(13);
        run(testInput, "480", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = 0L;
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < input.size(); i+=4) {
            String buttonALine = input.get(i);
            String[] split = buttonALine.split(" ");
            int ax = Integer.parseInt(split[2].substring(2).replaceAll(",", ""));
            int ay = Integer.parseInt(split[3].substring(2));
            Button a = new Button(BigDecimal.valueOf(ax), BigDecimal.valueOf(ay));
            String buttonBLine = input.get(i+1);
            split = buttonBLine.split(" ");
            int bx = Integer.parseInt(split[2].substring(2).replaceAll(",", ""));
            int by = Integer.parseInt(split[3].substring(2));
            Button b = new Button(BigDecimal.valueOf(bx), BigDecimal.valueOf(by));
            Pattern pattern = Pattern.compile("\\d+");
            String goalLine = input.get(i + 2);
            Matcher matcher = pattern.matcher(goalLine);
            matcher.find();
            int goalX = Integer.parseInt(matcher.group());
            matcher.find();
            int goalY = Integer.parseInt(matcher.group());
            games.add(new Game(a, b, new Goal(BigDecimal.valueOf(goalX + 10000000000000L), BigDecimal.valueOf(goalY + 10000000000000L))));

        }
        sum = games.stream().mapToLong(Thirteen::calculateMaxTokens).sum();

        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static long calculateMaxTokens(Game g) {
        BigDecimal subtract = g.a.x.multiply(g.b.y).subtract(g.a.y.multiply(g.b.x));
        BigDecimal a = g.g.x.multiply(g.b.y).subtract(g.g.y.multiply(g.b.x)).divide(subtract, MathContext.DECIMAL128);
        BigDecimal b = g.a.x.multiply(g.g.y).subtract(g.a.y.multiply(g.g.x)).divide(subtract, MathContext.DECIMAL128);
        if(a.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0 && b.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO)== 0){
            return a.multiply(BigDecimal.valueOf(3)).add(b).toBigInteger().longValue();
        }
        return 0;
//        if(ans > 0){
//            for (long i = ans; i > 0; i-= x) {
//                if(i % y == 0){
//                    long aPress = abs((ans-i)/x);
//                    long bPress = abs(i/y);
//                    if(aPress < 100 && bPress < 100){
//                        return (aPress * 3) + bPress;
//                    }
//                }
//            }
//        } else {
//            for (long i = ans; i < 0; i += x){
//                if(i % y == 0){
//                    long aPress = abs((ans-i)/x);
//                    long bPress = abs(i/y);
//                    if(aPress < 100 && bPress < 100){
//                        return (aPress * 3) + bPress;
//                    }
//                }
//            }
//        }
       // return 0;
    }

    private record Game(Button a, Button b, Goal g){}

    private record Button(BigDecimal x, BigDecimal y){}

    private record Goal(BigDecimal x, BigDecimal y){}

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
