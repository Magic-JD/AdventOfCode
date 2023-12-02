import tools.InternetParser;

import java.util.List;

import static java.lang.Math.max;
import static tools.LineUtils.extractInt;
import static tools.LineUtils.split;

public class Main {

    public static void main(String[] args) {
        List<String> strings = InternetParser.getInput(2);
        int count = 0;
        for (String string : strings) {
            String[] split = split(string, ":");
            int maxRed = Integer.MIN_VALUE;
            int maxGreen = Integer.MIN_VALUE;
            int maxBlue = Integer.MIN_VALUE;
            var draws = split(split[1], ";");
            for (String draw : draws) {
                String[] dice = split(draw, ",");
                for (String die : dice) {
                    String[] cd = split(die, " ");
                    var color = cd[1];
                    var no = extractInt(cd[0]);

                    if (color.equals("red")) {
                        maxRed = max(no, maxRed);
                    }
                    if (color.equals("blue")) {
                        maxBlue = max(no, maxBlue);
                    }
                    if (color.equals("green")) {
                        maxGreen = max(no, maxGreen);
                    }
                }
            }
            count += maxBlue * maxGreen * maxRed;
        }
        System.out.println(count);
    }

}