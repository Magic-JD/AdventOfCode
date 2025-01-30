package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BotCheck {

    public static final String testData = """
Year 2015 Day 1 - leaderboard overlap 80
Year 2015 Day 2 - leaderboard overlap 83
Year 2015 Day 3 - leaderboard overlap 87
Year 2015 Day 4 - leaderboard overlap 90
Year 2015 Day 5 - leaderboard overlap 75
Year 2015 Day 6 - leaderboard overlap 91
Year 2015 Day 7 - leaderboard overlap 96
Year 2015 Day 8 - leaderboard overlap 77
Year 2015 Day 9 - leaderboard overlap 97
Year 2015 Day 10 - leaderboard overlap 89
Year 2015 Day 11 - leaderboard overlap 97
Year 2015 Day 12 - leaderboard overlap 49
Year 2015 Day 13 - leaderboard overlap 92
Year 2015 Day 14 - leaderboard overlap 68
Year 2015 Day 15 - leaderboard overlap 91
Year 2015 Day 16 - leaderboard overlap 83
Year 2015 Day 17 - leaderboard overlap 88
Year 2015 Day 18 - leaderboard overlap 94
Year 2015 Day 19 - leaderboard overlap 48
Year 2015 Day 20 - leaderboard overlap 90
Year 2015 Day 21 - leaderboard overlap 89
Year 2015 Day 22 - leaderboard overlap 88
Year 2015 Day 23 - leaderboard overlap 99
Year 2015 Day 24 - leaderboard overlap 89
Year 2015 Day 25 - leaderboard overlap 96
Year 2016 Day 1 - leaderboard overlap 83
Year 2016 Day 2 - leaderboard overlap 71
Year 2016 Day 3 - leaderboard overlap 72
Year 2016 Day 4 - leaderboard overlap 89
Year 2016 Day 5 - leaderboard overlap 71
Year 2016 Day 6 - leaderboard overlap 85
Year 2016 Day 7 - leaderboard overlap 74
Year 2016 Day 8 - leaderboard overlap 89
Year 2016 Day 9 - leaderboard overlap 69
Year 2016 Day 10 - leaderboard overlap 91
Year 2016 Day 11 - leaderboard overlap 81
Year 2016 Day 12 - leaderboard overlap 92
Year 2016 Day 13 - leaderboard overlap 83
Year 2016 Day 14 - leaderboard overlap 75
Year 2016 Day 15 - leaderboard overlap 94
Year 2016 Day 16 - leaderboard overlap 79
Year 2016 Day 17 - leaderboard overlap 83
Year 2016 Day 18 - leaderboard overlap 95
Year 2016 Day 19 - leaderboard overlap 51
Year 2016 Day 20 - leaderboard overlap 63
Year 2016 Day 21 - leaderboard overlap 80
Year 2016 Day 22 - leaderboard overlap 50
Year 2016 Day 23 - leaderboard overlap 59
Year 2016 Day 24 - leaderboard overlap 93
Year 2016 Day 25 - leaderboard overlap 88
Year 2017 Day 1 - leaderboard overlap 76
Year 2017 Day 2 - leaderboard overlap 71
Year 2017 Day 3 - leaderboard overlap 49
Year 2017 Day 4 - leaderboard overlap 69
Year 2017 Day 5 - leaderboard overlap 77
Year 2017 Day 6 - leaderboard overlap 85
Year 2017 Day 7 - leaderboard overlap 42
Year 2017 Day 8 - leaderboard overlap 92
Year 2017 Day 9 - leaderboard overlap 89
Year 2017 Day 10 - leaderboard overlap 71
Year 2017 Day 11 - leaderboard overlap 72
Year 2017 Day 12 - leaderboard overlap 79
Year 2017 Day 13 - leaderboard overlap 63
Year 2017 Day 14 - leaderboard overlap 57
Year 2017 Day 15 - leaderboard overlap 73
Year 2017 Day 16 - leaderboard overlap 50
Year 2017 Day 17 - leaderboard overlap 54
Year 2017 Day 18 - leaderboard overlap 49
Year 2017 Day 19 - leaderboard overlap 86
Year 2017 Day 20 - leaderboard overlap 57
Year 2017 Day 21 - leaderboard overlap 91
Year 2017 Day 22 - leaderboard overlap 87
Year 2017 Day 23 - leaderboard overlap 41
Year 2017 Day 24 - leaderboard overlap 88
Year 2017 Day 25 - leaderboard overlap 91
Year 2018 Day 1 - leaderboard overlap 52
Year 2018 Day 2 - leaderboard overlap 72
Year 2018 Day 3 - leaderboard overlap 69
Year 2018 Day 4 - leaderboard overlap 87
Year 2018 Day 5 - leaderboard overlap 76
Year 2018 Day 6 - leaderboard overlap 61
Year 2018 Day 7 - leaderboard overlap 60
Year 2018 Day 8 - leaderboard overlap 74
Year 2018 Day 9 - leaderboard overlap 43
Year 2018 Day 10 - leaderboard overlap 94
Year 2018 Day 11 - leaderboard overlap 49
Year 2018 Day 12 - leaderboard overlap 63
Year 2018 Day 13 - leaderboard overlap 70
Year 2018 Day 14 - leaderboard overlap 58
Year 2018 Day 15 - leaderboard overlap 81
Year 2018 Day 16 - leaderboard overlap 76
Year 2018 Day 17 - leaderboard overlap 95
Year 2018 Day 18 - leaderboard overlap 71
Year 2018 Day 19 - leaderboard overlap 45
Year 2018 Day 20 - leaderboard overlap 86
Year 2018 Day 21 - leaderboard overlap 44
Year 2018 Day 22 - leaderboard overlap 54
Year 2018 Day 23 - leaderboard overlap 41
Year 2018 Day 24 - leaderboard overlap 83
Year 2018 Day 25 - leaderboard overlap 81
Year 2019 Day 1 - leaderboard overlap 55
Year 2019 Day 2 - leaderboard overlap 73
Year 2019 Day 3 - leaderboard overlap 71
Year 2019 Day 4 - leaderboard overlap 56
Year 2019 Day 5 - leaderboard overlap 74
Year 2019 Day 6 - leaderboard overlap 75
Year 2019 Day 7 - leaderboard overlap 50
Year 2019 Day 8 - leaderboard overlap 62
Year 2019 Day 9 - leaderboard overlap 97
Year 2019 Day 10 - leaderboard overlap 64
Year 2019 Day 11 - leaderboard overlap 81
Year 2019 Day 12 - leaderboard overlap 50
Year 2019 Day 13 - leaderboard overlap 25
Year 2019 Day 14 - leaderboard overlap 71
Year 2019 Day 15 - leaderboard overlap 76
Year 2019 Day 16 - leaderboard overlap 43
Year 2019 Day 17 - leaderboard overlap 39
Year 2019 Day 18 - leaderboard overlap 51
Year 2019 Day 19 - leaderboard overlap 42
Year 2019 Day 20 - leaderboard overlap 61
Year 2019 Day 21 - leaderboard overlap 47
Year 2019 Day 22 - leaderboard overlap 42
Year 2019 Day 23 - leaderboard overlap 79
Year 2019 Day 24 - leaderboard overlap 66
Year 2019 Day 25 - leaderboard overlap 82
Year 2020 Day 1 - leaderboard overlap 36
Year 2020 Day 2 - leaderboard overlap 72
Year 2020 Day 3 - leaderboard overlap 58
Year 2020 Day 4 - leaderboard overlap 43
Year 2020 Day 5 - leaderboard overlap 71
Year 2020 Day 6 - leaderboard overlap 52
Year 2020 Day 7 - leaderboard overlap 62
Year 2020 Day 8 - leaderboard overlap 58
Year 2020 Day 9 - leaderboard overlap 62
Year 2020 Day 10 - leaderboard overlap 53
Year 2020 Day 11 - leaderboard overlap 61
Year 2020 Day 12 - leaderboard overlap 53
Year 2020 Day 13 - leaderboard overlap 41
Year 2020 Day 14 - leaderboard overlap 61
Year 2020 Day 15 - leaderboard overlap 36
Year 2020 Day 16 - leaderboard overlap 47
Year 2020 Day 17 - leaderboard overlap 84
Year 2020 Day 18 - leaderboard overlap 65
Year 2020 Day 19 - leaderboard overlap 51
Year 2020 Day 20 - leaderboard overlap 28
Year 2020 Day 21 - leaderboard overlap 75
Year 2020 Day 22 - leaderboard overlap 42
Year 2020 Day 23 - leaderboard overlap 37
Year 2020 Day 24 - leaderboard overlap 68
Year 2020 Day 25 - leaderboard overlap 92
Year 2021 Day 1 - leaderboard overlap 62
Year 2021 Day 2 - leaderboard overlap 62
Year 2021 Day 3 - leaderboard overlap 56
Year 2021 Day 4 - leaderboard overlap 71
Year 2021 Day 5 - leaderboard overlap 66
Year 2021 Day 6 - leaderboard overlap 36
Year 2021 Day 7 - leaderboard overlap 55
Year 2021 Day 8 - leaderboard overlap 35
Year 2021 Day 9 - leaderboard overlap 65
Year 2021 Day 10 - leaderboard overlap 67
Year 2021 Day 11 - leaderboard overlap 86
Year 2021 Day 12 - leaderboard overlap 49
Year 2021 Day 13 - leaderboard overlap 58
Year 2021 Day 14 - leaderboard overlap 49
Year 2021 Day 15 - leaderboard overlap 39
Year 2021 Day 16 - leaderboard overlap 79
Year 2021 Day 17 - leaderboard overlap 55
Year 2021 Day 18 - leaderboard overlap 87
Year 2021 Day 19 - leaderboard overlap 83
Year 2021 Day 20 - leaderboard overlap 77
Year 2021 Day 21 - leaderboard overlap 44
Year 2021 Day 22 - leaderboard overlap 28
Year 2021 Day 23 - leaderboard overlap 14
Year 2021 Day 24 - leaderboard overlap 87
Year 2021 Day 25 - leaderboard overlap 88
Year 2022 Day 1 - leaderboard overlap 73
Year 2022 Day 2 - leaderboard overlap 57
Year 2022 Day 3 - leaderboard overlap 69
Year 2022 Day 4 - leaderboard overlap 67
Year 2022 Day 5 - leaderboard overlap 86
Year 2022 Day 6 - leaderboard overlap 76
Year 2022 Day 7 - leaderboard overlap 75
Year 2022 Day 8 - leaderboard overlap 57
Year 2022 Day 9 - leaderboard overlap 43
Year 2022 Day 10 - leaderboard overlap 57
Year 2022 Day 11 - leaderboard overlap 65
Year 2022 Day 12 - leaderboard overlap 87
Year 2022 Day 13 - leaderboard overlap 82
Year 2022 Day 14 - leaderboard overlap 82
Year 2022 Day 15 - leaderboard overlap 48
Year 2022 Day 16 - leaderboard overlap 35
Year 2022 Day 17 - leaderboard overlap 41
Year 2022 Day 18 - leaderboard overlap 43
Year 2022 Day 19 - leaderboard overlap 46
Year 2022 Day 20 - leaderboard overlap 68
Year 2022 Day 21 - leaderboard overlap 38
Year 2022 Day 22 - leaderboard overlap 42
Year 2022 Day 23 - leaderboard overlap 88
Year 2022 Day 24 - leaderboard overlap 85
Year 2022 Day 25 - leaderboard overlap 91
Year 2023 Day 1 - leaderboard overlap 30
Year 2023 Day 2 - leaderboard overlap 76
Year 2023 Day 3 - leaderboard overlap 67
Year 2023 Day 4 - leaderboard overlap 51
Year 2023 Day 5 - leaderboard overlap 35
Year 2023 Day 6 - leaderboard overlap 52
Year 2023 Day 7 - leaderboard overlap 62
Year 2023 Day 8 - leaderboard overlap 37
Year 2023 Day 9 - leaderboard overlap 72
Year 2023 Day 10 - leaderboard overlap 33
Year 2023 Day 11 - leaderboard overlap 55
Year 2023 Day 12 - leaderboard overlap 22
Year 2023 Day 13 - leaderboard overlap 48
Year 2023 Day 14 - leaderboard overlap 44
Year 2023 Day 15 - leaderboard overlap 45
Year 2023 Day 16 - leaderboard overlap 82
Year 2023 Day 17 - leaderboard overlap 67
Year 2023 Day 18 - leaderboard overlap 40
Year 2023 Day 19 - leaderboard overlap 47
Year 2023 Day 20 - leaderboard overlap 37
Year 2023 Day 21 - leaderboard overlap 24
Year 2023 Day 22 - leaderboard overlap 72
Year 2023 Day 23 - leaderboard overlap 25
Year 2023 Day 24 - leaderboard overlap 36
Year 2023 Day 25 - leaderboard overlap 93

""";


// 3, 32, 78, 62, 58, 75
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        int[] s1 = testInput.stream().map(s -> s.split(" ")).map(ar -> ar[ar.length - 1]).mapToInt(s -> Integer.parseInt(s)).toArray();//.sorted().forEach(i -> System.out.println(i));
        int count = 0;
        for (int i = 0; i < s1.length; i++) {
            count += s1[i];
        }
        System.out.println(count / (double) s1.length);
//        Set<String> first = new HashSet<>(testInput.subList(0, 100));
//        List<String> second = new ArrayList<>(testInput.subList(100, testInput.size()));
//        int count = 0;
//        for (int i = 0; i < second.size(); i++) {
//            if(!first.add(second.get(i))){
//                count += 1;
//            }
//        }
//        System.out.println(count);
//        for (int k = 15; k < 24; k++) {
//            for (int i = 1; i <= 25; i++) {
//                List<String> mainInput = InternetParser.getLeader(k, i);
//                String allWithRelevantData = mainInput.stream().filter(s -> s.contains("data-user-id=")).collect(Collectors.joining());
//                Pattern pattern = Pattern.compile("data-user-id=\"\\d*\"");
//                Matcher matcher = pattern.matcher(allWithRelevantData);
//                List<String> testInput = new ArrayList<>();
//                while (matcher.find()) {
//                    testInput.add(matcher.group());
//                }
//                Set<String> first = new HashSet<>(testInput.subList(0, 100));
//                List<String> second = new ArrayList<>(testInput.subList(100, testInput.size()));
//                int count = 0;
//                for (int j = 0; j < second.size(); j++) {
//                    if (!first.add(second.get(j))) {
//                        count += 1;
//                    }
//                }
//                System.out.println("Year 20" + k + " Day " + i + " - leaderboard overlap " + count);
//            }
//        }
        //run(testInput, "368", System.currentTimeMillis());run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = 0L;
        char[][] map = input.stream().map(String::trim).map(String::toCharArray).toArray(char[][]::new);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                char c = map[i][j];
                if(c != '.'){
                    sum += processMap(i, j, map);
                }
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static long processMap(int i, int j, char[][] map) {
        long p = 0;
        char c = map[i][j];

        Set<Loc> seen = new HashSet<>();
        List<Loc> around = new ArrayList<>();
        ArrayDeque<Loc> arrayDeque = new ArrayDeque<>();
        List<PerH> perH = new ArrayList<>();
        List<PerV> perV = new ArrayList<>();
        Loc current = new Loc(i, j, c);
        arrayDeque.add(current);
        seen.add(current);
        while (!arrayDeque.isEmpty()){
            Loc ex = arrayDeque.pop();
            int i1 = ex.i;
            int j1 = ex.j;
            try{
                Loc e = new Loc(i1 + 1, j1, map[i1 + 1][j1]);
                around.add(e);
                if(e.c != ex.c){
                    perH.add(new PerH((i1*2)+1, j1));
                }
            } catch (IndexOutOfBoundsException e){
                perH.add(new PerH((i1*2)+1, j1));
            }
            try{
                Loc e = new Loc(i1 - 1, j1, map[i1 - 1][j1]);
                around.add(e);
                if(e.c != ex.c){
                    perH.add(new PerH((i1*2)-1, j1));
                }
            } catch (IndexOutOfBoundsException e){
                perH.add(new PerH((i1*2)-1, j1));
            }
            try{
                Loc e = new Loc(i1, j1 + 1, map[i1][j1 + 1]);
                around.add(e);
                if(e.c != ex.c){
                    perV.add(new PerV(i1, (j1*2) + 1));
                }
            } catch (IndexOutOfBoundsException e){
                perV.add(new PerV(i1, (j1*2) + 1));
            }
            try{
                Loc e = new Loc(i1, j1 - 1, map[i1][j1 - 1]);
                around.add(e);
                if(e.c != ex.c){
                    perV.add(new PerV(i1, (j1*2) - 1));
                }
            } catch (IndexOutOfBoundsException e){
                perV.add(new PerV(i1, (j1*2) - 1));
            }
            arrayDeque.addAll(around.stream().filter(l -> l.c == ex.c).filter(seen::add).toList());
            around = new ArrayList<>();
        }
        seen.forEach(l -> map[l.i][l.j] = '.');
        return (long) seen.size() * calculateSides(perH, perV);
    }

    private record PerV(int i, int j){}
    private record PerH(int i, int j){}

    private static int calculateSides(List<PerH> perHs, List<PerV> perVs) {
        Map<Integer, List<PerH>> tops = perHs.stream().collect(Collectors.groupingBy(l -> l.i));
        Map<Integer, List<PerV>> bottoms = perVs.stream().collect(Collectors.groupingBy(l -> l.j));
        AtomicInteger perimeter = new AtomicInteger(tops.size() + bottoms.size());
        tops.forEach((in, locs) -> {
            List<PerH> list = locs.stream().sorted(Comparator.comparingInt(l -> l.j)).toList();
            int start = list.getFirst().j();
            for (int i = 1; i < list.size(); i++) {
                int currentJ = list.get(i).j;
                if(currentJ - start == 1 && bottoms.entrySet().stream().noneMatch(ent -> ent.getKey() == currentJ + (currentJ-1) && ent.getValue().stream().anyMatch(l -> in/2 == l.i))){
                    start = currentJ;
                } else {
                    start = currentJ;
                    perimeter.addAndGet(1);
                }
            }

        });
        bottoms.forEach((in, locs) -> {
            List<PerV> list = locs.stream().sorted(Comparator.comparingInt(l -> l.i)).toList();
            int start = list.getFirst().i();
            for (int i = 1; i < list.size(); i++) {
                int currentI = list.get(i).i;
                if(currentI - start == 1 && tops.entrySet().stream().noneMatch(ent -> ent.getKey() == currentI + (currentI-1) && ent.getValue().stream().anyMatch(l -> in/2 == l.j))){
                    start = currentI;
                } else {
                    start = currentI;
                    perimeter.addAndGet(1);
                }
            }

        });
        return perimeter.get();
    }

    private record Loc(int i, int j, char c){}


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
