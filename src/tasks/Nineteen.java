package tasks;

import tools.InternetParser;

import java.util.*;
import java.util.function.Function;

public class Nineteen {

    public static final String testData = """   
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
                                                       
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
                                           """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(19);
        run(testInput, "167409079868000", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<String, Workflow> wf = new HashMap<>();
        boolean workflow = true;
        for (String string : input) {
            if (string.isBlank()) {
                workflow = false;
            }
            if (workflow) {
                String[] split = string.split("\\{");
                String key = split[0];
                String[] wfString = split[1].replace("}", "").split(",");
                Workflow newWorkflow = new Workflow(Arrays.stream(wfString).map(s -> {
                    String[] opToResult = s.split(":");
                    boolean isEnd = opToResult.length == 1;
                    if (isEnd) return new Ops(part -> new Result(null, part, opToResult[0]));
                    boolean greaterThan;
                    String[] operands;
                    if (opToResult[0].contains("<")) {
                        greaterThan = false;
                        operands = opToResult[0].split("<");
                    } else {
                        operands = opToResult[0].split(">");
                        greaterThan = true;
                    }
                    final int value = Integer.parseInt(operands[1]);
                    return new Ops(part -> {
                        Part leftPart = null;
                        Part rightPart = null;
                        switch (operands[0]) {
                            case "x" -> {
                                if (part.xS < value && part.xE > value) {
                                    if (greaterThan) {
                                        leftPart = new Part(part.xS, value, part.mS, part.mE, part.aS, part.aE, part.sS, part.sE);
                                        rightPart = new Part(value+1, part.xE, part.mS, part.mE, part.aS, part.aE, part.sS, part.sE);
                                    } else {
                                        leftPart = new Part(value, part.xE, part.mS, part.mE, part.aS, part.aE, part.sS, part.sE);
                                        rightPart = new Part(part.xS, value-1, part.mS, part.mE, part.aS, part.aE, part.sS, part.sE);
                                    }
                                } else if ((greaterThan && part.xE <= value) || (!greaterThan && part.xS >= value)) {
                                    leftPart = part;
                                } else {
                                    rightPart = part;
                                }
                            }
                            case "m" -> {
                                if (part.mS < value && part.mE > value) {
                                    if (greaterThan) {
                                        leftPart = new Part(part.xS, part.xE, part.mS, value, part.aS, part.aE, part.sS, part.sE);
                                        rightPart = new Part(part.xS, part.xE, value+1, part.mE, part.aS, part.aE, part.sS, part.sE);
                                    } else {
                                        leftPart = new Part(part.xS, part.xE, value, part.mE, part.aS, part.aE, part.sS, part.sE);
                                        rightPart = new Part(part.xS, part.xE, part.mS, value-1, part.aS, part.aE, part.sS, part.sE);
                                    }
                                } else if ((greaterThan && part.mE <= value) || (!greaterThan && part.mS >= value)) {
                                    leftPart = part;
                                } else {
                                    rightPart = part;
                                }
                            }
                            case "a" -> {
                                if (part.aS < value && part.aE > value) {
                                    if (greaterThan) {
                                        leftPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, value, part.sS, part.sE);
                                        rightPart = new Part(part.xS, part.xE, part.mS, part.mE, value+1, part.aE, part.sS, part.sE);
                                    } else {
                                        leftPart = new Part(part.xS, part.xE, part.mS, part.mE, value, part.aE, part.sS, part.sE);
                                        rightPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, value-1, part.sS, part.sE);
                                    }
                                } else if ((greaterThan && part.aE <= value) || (!greaterThan && part.aS >= value)) {
                                    leftPart = part;
                                } else {
                                    rightPart = part;
                                }
                            }
                            case "s" -> {
                                if (part.sS < value && part.sE > value) {
                                    if (greaterThan) {
                                        leftPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, part.aE, part.sS, value);
                                        rightPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, part.aE, value+1, part.sE);
                                    } else {
                                        leftPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, part.aE, value, part.sE);
                                        rightPart = new Part(part.xS, part.xE, part.mS, part.mE, part.aS, part.aE, part.sS, value-1);
                                    }
                                } else if ((greaterThan && part.sE <= value) || (!greaterThan && part.sS >= value)) {
                                    leftPart = part;
                                } else {
                                    rightPart = part;
                                }
                            }
                        }
                        //System.out.println(leftPart + " " + rightPart + " " + opToResult[0]);
                        return new Result(leftPart, rightPart, opToResult[1]);
                    });
                }).toList());
                wf.put(key, newWorkflow);
            }
        }
        List<Part> allPartSubDivisions = checkWorkflow("in", wf, new Part(1, 4000, 1, 4000, 1,
                4000, 1, 4000));
        var in = allPartSubDivisions.stream().mapToLong(part -> ((part.xE - part.xS)+1) * ((part.mE - part.mS)+1) * ((part.aE - part.aS)+1)
                * ((part.sE - part.sS)+1)).sum();
        long sum = in;
        var answer = "" + sum;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static List<Part> checkWorkflow(String workflow,
                                            Map<String, Workflow> wf, Part part) {
        if(workflow.equals("A")){
            return List.of(part);
        }
        if(workflow.equals("R")){
            return Collections.emptyList();
        }
        Workflow foundFlow = wf.get(workflow);
        List<Part> foundParts = new ArrayList<>();
        Part partToCheck = part;
        for (int i = 0; i < foundFlow.ops.size(); i++) {
            Ops op = foundFlow.ops.get(i);
            Result apply = op.check.apply(partToCheck);
            partToCheck = apply.leftPart;
            Part partToResend = apply.rightPart;
            if(partToResend != null){
                foundParts.addAll(checkWorkflow(apply.rightString, wf, partToResend));
            }
            if(partToResend == null){
                return foundParts;
            }

        }
        return foundParts;
    }

    private record Ops(Function<Part, Result> check) {
    }

    private record Workflow(List<Ops> ops) {
    }

    private record Result(Part leftPart, Part rightPart, String rightString) {
    }

    private record Part(long xS, long xE, long mS, long mE, long aS, long aE,
                        long sS, long sE) {
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

}