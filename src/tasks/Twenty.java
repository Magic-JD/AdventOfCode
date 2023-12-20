package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Twenty {

    public static final String testData = """      
            """;

    public static long HIGH_ENERGY = 0L;
    public static long LOW_ENERGY = 0L;

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(20);
        //run(testInput, "-", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<String, Op> mapping = new HashMap<>();
        mapping.put("output", new Output());
        for (String s : input) {
            if (s.startsWith("%")) {
                s = s.substring(1);
                String[] split = s.split(" -> ");
                Flip flip = new Flip(Arrays.stream(split[1].split(", ")).toList(), split[0]);
                mapping.put(flip.name, flip);
            } else if (s.startsWith("&")) {
                s = s.substring(1);
                String[] split = s.split(" -> ");
                Con con = new Con(Arrays.stream(split[1].split(", ")).toList(), split[0]);
                mapping.put(con.name, con);
            } else {
                String[] split = s.split(" -> ");
                LowSpreader l = new
                        LowSpreader(Arrays.stream(split[1].split(", ")).toList(), split[0]);
                mapping.put(l.name, l);
            }
        }
        mapping.values().forEach(op -> op.outputTo().forEach(o -> {
            if (mapping.get(o) instanceof Con c)
                c.map.put(op.getName(), false);
        }));
        LowSpreader button = new LowSpreader(List.of("broadcaster"),
                "button");
        ArrayDeque<Supplier<Result>> arrayDeque = new ArrayDeque<>();
        int i;
        for (i = 0; i < 10000000; i++) {
            int finalI = i;
            arrayDeque.add(() -> button.relay(new
                    Result(finalI +1, List.of("broadcaster"), false, button.name)));
            while (!arrayDeque.isEmpty()) {
                Result result = arrayDeque.pop().get();
                arrayDeque.addAll(result.to.stream().map(mapping::get).filter(Objects::nonNull).map(op
                        -> ((Supplier<Result>) () -> op.relay(result))).toList());
            }
        }
        var answer = "" + i;
        showAnswer(answer, expectedOutput, startTime);
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

    private interface Op {
        Result relay(Result result);

        List<String> outputTo();

        String getName();
    }

    private static class Output implements Op {

        @Override
        public Result relay(Result result) {
            return new Result(result.run, Collections.emptyList(), false, "output");
        }

        @Override
        public List<String> outputTo() {
            return Collections.emptyList();
        }

        @Override
        public String getName() {
            return "output";
        }
    }

    private static class LowSpreader implements Op {

        private final String name;
        private final List<String> outputTo;

        public LowSpreader(List<String> outputTo, String name) {
            this.outputTo = outputTo;
            this.name = name;
        }

        @Override
        public Result relay(Result result) {
            LOW_ENERGY += outputTo.size();
            return new Result(result.run, outputTo, false, name);
        }

        @Override
        public List<String> outputTo() {
            return outputTo;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static class Con implements Op {

        Map<String, Boolean> map = new HashMap<>();
        List<String> outputTo;
        String name;
        boolean interesting = false;

        public Con(List<String> to, String name) {
            outputTo = to;
            this.name = name;
            if(to.contains("rx")){
                interesting = true;
            }
        }


        @Override
        public Result relay(Result result) {
            map.put(result.from, result.energy);
            if(interesting){
                if(map.values().contains(true)){
                    Object[] array = map.values().toArray();
                    //for (int i = 0; i < array.length; i++) {
                        if(array[3].equals(true)){ //I was too lazy to write the proper algorithm for this
                            // so instead I just logged the cycles and then put them through a LCM calculator to get the answer
                            System.out.println(result.run);
                        }
                    //}
                    //System.out.println(Arrays.toString(array));
                }
            }
            if (map.values().stream().allMatch(b -> b)) {
                LOW_ENERGY += outputTo.size();
                return new Result(result.run, outputTo, false, name);
            } else {
                HIGH_ENERGY += outputTo.size();
                return new Result(result.run,  outputTo, true, name);
            }
        }

        @Override
        public List<String> outputTo() {
            return outputTo;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    private static class Flip implements Op {
        boolean on = false;
        List<String> outputTo;
        String name;


        public Flip(List<String> outputTo, String name) {
            this.outputTo = outputTo;
            this.name = name;
        }


        @Override
        public Result relay(Result result) {
            boolean energy = result.energy;
            if (energy) {
                return new Result(result.run, Collections.emptyList(), false, name);
            }
            on = !on;
            if (on) {
                HIGH_ENERGY += outputTo.size();
            } else {
                LOW_ENERGY += outputTo.size();
            }
            return new Result(result.run, outputTo, on, name);
        }

        @Override
        public List<String> outputTo() {
            return outputTo;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private record Result(long run, List<String> to, boolean energy, String from) {

        @Override
        public String toString() {
            String e = energy ? "high" : "low";
            return to.stream().map(t -> from + " -" + e + "-> " +
                    t).collect(Collectors.joining("\n"));
        }
    }
}