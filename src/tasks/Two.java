package tasks;

import tools.FileParser;
import tools.InternetParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Two {

    private static final Map<String, Integer> colorToMax = Map.of("blue", 14, "red", 12, "green", 13);

    public static void main(String[] args) {
        FileParser fileParser = new FileParser();
        List<String> strings = new InternetParser().getInput(2);
        AtomicInteger count = new AtomicInteger();
        strings.forEach(s -> {
                    String[] split = s.split(":");
                    AtomicInteger maxRed = new AtomicInteger();
                    AtomicInteger maxGreen = new AtomicInteger();
                    AtomicInteger maxBlue = new AtomicInteger();
                    var draws = split[1].split(";");
                    Arrays.stream(draws).forEach(st -> {
                        Arrays.stream(st.split(",")).map(s4 -> s4.trim()).forEach(col -> {
                            String[] split1 = col.split(" ");
                            int no = Integer.parseInt(split1[0].trim());
                            String color = split1[1].trim();
                            if (color.equals("red")) {
                                maxRed.set(Math.max(no, maxRed.get()));
                            }
                            if (color.equals("blue")) {
                                maxBlue.set(Math.max(no, maxBlue.get()));
                            }
                            if (color.equals("green")) {
                                maxGreen.set(Math.max(no, maxGreen.get()));
                            }
                        });
                    });
                    count.addAndGet((maxGreen.get() * maxBlue.get() * maxRed.get()));
                }
        );
        System.out.println(count.get());
    }

}