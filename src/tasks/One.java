package tasks;

import tools.FileParser;
import tools.InternetParser;

import java.util.*;

public class One {
    public static void main(String[] args) {
        List<String> strings = InternetParser.getInput(1);
        final List<Integer> first = new ArrayList<>();
        final Map<Integer, Integer> second = new HashMap<>();
        long sum = 0L;
        strings.forEach(s -> {
            String[] s1 = s.split(" {3}");
            first.add(Integer.valueOf(s1[0]));
            Integer key = Integer.valueOf(s1[1]);
            second.put(key, second.getOrDefault(key, 0) + 1);
        });
        Collections.sort(first);
        for (int i = 0; i < first.size(); i++) {
            Integer f = first.get(i);
            sum += (long) second.getOrDefault(f, 0) * f;
        }
        System.out.println(sum);
    }
}