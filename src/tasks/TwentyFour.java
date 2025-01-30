package tasks;

import tools.InternetParser;

import java.util.*;

public class TwentyFour {

    public static final String testData = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
                        """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(24);
        // run(testInput, "44", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<String, Integer> mem = new HashMap<>();
        List<Op> ops = new ArrayList<>();
        boolean isInit = true;
        for (String s : input) {
            if (isInit) {
                if (s.isBlank()) {
                    isInit = false;
                } else {
                    String[] split = s.split(": ");
                    mem.put(split[0], new Random().nextInt(2));
                }
            } else {
                String[] split = s.split(" ");
                Operation operation = switch (split[1]) {
                    case "AND" -> (a, b) -> a & b;
                    case "OR" -> (a, b) -> a | b;
                    case "XOR" -> (a, b) -> a ^ b;
                    default -> throw new UnsupportedOperationException();
                };
                ops.add(new Op(split[0], split[2], split[1], operation, split[4]));
            }
        }
        // z21 -> these two need to be swapped with a lower layer -> structure = x&y -> rand -> rand -> z
        // Two of the ones that are in the bottom layer should go to Z but instead go up.
        //
        // snp, mnh, OR, z15 && ksm, fcv, AND, z34  -> These two also need to swap.
        // Above instructions belong one level above...
        // cqk,fph,gds,z15,z21,z34
        //
        // ccp, hhw, XOR, fph
        // nsp, tqh, XOR, gds
        // ksm, fcv, XOR, cqk


        // gds & z21 should swap
        // fph & z15
        // cqk & z34

        //cqk, fhp, z15, z34

        // gds, vqn, OR, fsh

        //ops.stream().filter(op -> op.in1.startsWith("y") || op.in2.startsWith("y")).sorted(Comparator.comparing(op1 -> op1.in1.substring(1) + op1.key)).forEach(op -> System.out.println(op.in1 + ", " + op.in2 + ", " + op.key + ", " + op.out));

        //ops.stream().filter(op -> op.out.startsWith("z")).sorted(Comparator.comparing(op -> op.out)).filter(op -> !op.key.equals("XOR")).forEach(op -> System.out.println(op.in1 + ", " + op.in2 + ", " + op.key + ", " + op.out));
        List<String> answerList = List.of("wrk", "jrs"
                ,"fph", "gds", "cqk", "z15", "z21", "z34").stream().sorted().toList();
        System.out.println(String.join(",", answerList));

//        List<String> test = ops.stream().filter(op -> op.in1.startsWith("y") || op.in2.startsWith("y")).filter(op -> !op.out.startsWith("z")).map(op -> op.out).toList();
//     ops.stream().filter(op -> !op.in1.startsWith("y") && !op.in1.startsWith("x") && !op.out.startsWith("z")).filter(op ->!op.key.equals("XOR")).sorted(Comparator.comparing(op -> op.in1)).filter(op -> !test.contains(op.in1) && !test.contains(op.in2)).forEach(op -> System.out.println(op.in1 + ", " + op.in2 + ", " + op.key + ", " + op.out));

//        for (int i = 130; i < 135; i++) {
//            String index = String.valueOf(i).substring(1);
//            Set<Op> seen = new HashSet<>();
//            List<Op> list = ops.stream().filter(op -> op.in1.endsWith(index)).sorted(Comparator.comparing(Op::key)).toList();
//            list.forEach(System.out::println);
//            int count = list.size();
//            while (!list.isEmpty()){
//                list = list.stream().flatMap(o -> ops.stream().filter(opp -> opp.in1.equals(o.out) || opp.in2.equals(o.out))).filter(seen::add).toList();
//                list.forEach(System.out::println);
//                count += list.size();
//            }
//            System.out.println("Count " + count);
//            System.out.println("-----------------------------------------------------");
//
//        }

        long firstn = convertRegisterToNumber(mem, "x");
        long secondn = convertRegisterToNumber(mem, "y");
        List<Op> copy = new ArrayList<>(ops);
        Map<String, Integer> copym = new HashMap<>(mem);
        System.out.println(ops.size());
        List<String> possibilities = new ArrayList<>();
        possibilities.add("hjm");
        possibilities.add("hpt");
        possibilities.add("pct");
        possibilities.add("khs");
        possibilities.add("qkf");
        possibilities.add("gpr");
        possibilities.add("bwv");
        possibilities.add("ksm");
        possibilities.add("cqk");
        possibilities.add("bjj");
        possibilities.add("fwm");
        possibilities.add("nmb");
        possibilities.add("jbc");
        possibilities.add("jvv");
        possibilities.add("nrd");
        possibilities.add("bbt");
        possibilities.add("chp");
        possibilities.add("pjj");
        for (int i = 0; i < copy.size(); i++) {
            for (int j = i; j < copy.size(); j++) {
                ops = new ArrayList<>(copy);
                Op first = copy.get(i);
                Op second = copy.get(j);
                ops.remove(first);
                ops.remove(second);
                ops.add(new Op(first.in1, first.in2, first.key, first.operation, second.out));
                ops.add(new Op(second.in1, second.in2, second.key, second.operation, first.out));
                mem = new HashMap<>(copym);
                while (!ops.isEmpty()) {
                    boolean opsHasReduced = false;
                    List<Op> toRemove = new ArrayList<>();
                    for (Op op : ops) {
                        if (mem.containsKey(op.in1) && mem.containsKey(op.in2)) {
                            toRemove.add(op);
                            opsHasReduced = true;
                            mem.put(op.out, op.operation.handle(mem.get(op.in1), mem.get(op.in2)));
                        }
                    }
                    if(opsHasReduced){
                        ops.removeAll(toRemove);
                    } else {
                        ops = new ArrayList<>();
                    }
                }
                long answer = convertRegisterToNumber(mem, "z");
                if (answer == firstn + secondn) {
                    System.out.println(first.out + ", " + second.out);
                }
            }
        }


//        System.out.println(first);
//        System.out.println(second);
//        System.out.println(expected);
//        System.out.println(answer);
//        System.out.println(Long.toBinaryString(first));
//        System.out.println(Long.toBinaryString(second));
//        System.out.println(Long.toBinaryString(expected));
//        System.out.println(Long.toBinaryString(answer));
//        System.out.println(Long.toBinaryString(expected ^ answer));


        // showAnswer(String.valueOf(answer), expectedOutput, startTime);
    }

    private static long convertRegisterToNumber(Map<String, Integer> mem, String z) {
        List<String> sorted = mem.keySet().stream().filter(v -> v.startsWith(z)).sorted(Comparator.reverseOrder()).toList();
        long answer = 0;
        for (String s : sorted) {
            answer += mem.get(s);
            answer <<= 1;
        }
        answer >>= 1;
        return answer;
    }

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

    private interface Operation {
        int handle(int a, int b);
    }

    private record Op(String in1, String in2, String key, Operation operation, String out) {
        @Override
        public String toString() {
            return in1 + ", " + in2 + ", " + key + ", " + out;
        }
    }
}
