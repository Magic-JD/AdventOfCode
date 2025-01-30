package twentytwentythree;

import tools.InternetParser;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(19);
        run(testInput, "167409079868000", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Map<String, Workflow> workflows = new HashMap<>();
        boolean isWorkflow = true;

        for (String line : input) {
            if (line.isBlank()) {
                isWorkflow = false;
                continue;
            }
            if (isWorkflow) {
                String key = line.split("\\{")[0];
                String[] wfString = line.split("\\{")[1].replace("}", "").split(",");
                Workflow workflow = new Workflow(Arrays.stream(wfString).map(Nineteen::parseOps).collect(Collectors.toList()));
                workflows.put(key, workflow);
            }
        }

        List<Part> parts = processWorkflow("in", workflows, new Part(1, 4000, 1, 4000, 1, 4000, 1, 4000));
        long totalVolume = parts.stream().mapToLong(Part::calculateVolume).sum();

        showAnswer(String.valueOf(totalVolume), expectedOutput, startTime);
    }

    private static Ops parseOps(String s) {
        String[] opToResult = s.split(":");
        boolean isEnd = opToResult.length == 1;

        if (isEnd) {
            return new Ops(part -> new Result(null, part, opToResult[0]));
        }

        boolean greaterThan = opToResult[0].contains(">");
        String[] operands = opToResult[0].split(greaterThan ? ">" : "<");
        int value = Integer.parseInt(operands[1]);

        return new Ops(part -> {
            Part leftPart = null;
            Part rightPart = null;

            switch (operands[0]) {
                case "x":
                    if (part.xS < value && part.xE > value) {
                        leftPart = part.splitX(value, greaterThan);
                        rightPart = part.splitX(value, !greaterThan);
                    } else if ((greaterThan && part.xE <= value) || (!greaterThan && part.xS >= value)) {
                        leftPart = part;
                    } else {
                        rightPart = part;
                    }
                    break;
                case "m":
                    if (part.mS < value && part.mE > value) {
                        leftPart = part.splitM(value, greaterThan);
                        rightPart = part.splitM(value, !greaterThan);
                    } else if ((greaterThan && part.mE <= value) || (!greaterThan && part.mS >= value)) {
                        leftPart = part;
                    } else {
                        rightPart = part;
                    }
                    break;
                case "a":
                    if (part.aS < value && part.aE > value) {
                        leftPart = part.splitA(value, greaterThan);
                        rightPart = part.splitA(value, !greaterThan);
                    } else if ((greaterThan && part.aE <= value) || (!greaterThan && part.aS >= value)) {
                        leftPart = part;
                    } else {
                        rightPart = part;
                    }
                    break;
                case "s":
                    if (part.sS < value && part.sE > value) {
                        leftPart = part.splitS(value, greaterThan);
                        rightPart = part.splitS(value, !greaterThan);
                    } else if ((greaterThan && part.sE <= value) || (!greaterThan && part.sS >= value)) {
                        leftPart = part;
                    } else {
                        rightPart = part;
                    }
                    break;
            }

            return new Result(leftPart, rightPart, opToResult[1]);
        });
    }

    private static List<Part> processWorkflow(String workflow, Map<String, Workflow> workflows, Part part) {
        if (workflow.equals("A")) {
            return List.of(part);
        }
        if (workflow.equals("R") || part == null) {
            return Collections.emptyList();
        }

        Workflow foundFlow = workflows.get(workflow);
        if (foundFlow == null) {
            return Collections.emptyList();
        }

        List<Part> foundParts = new ArrayList<>();
        Part partToCheck = part;

        for (Ops op : foundFlow.ops) {
            Result apply = op.check.apply(partToCheck);
            partToCheck = apply.leftPart;
            Part partToResend = apply.rightPart;

            if (partToResend != null) {
                foundParts.addAll(processWorkflow(apply.rightString, workflows, partToResend));
            }

            if (partToCheck == null) {
                break;
            }
        }
        if (partToCheck != null) {
            foundParts.add(partToCheck);
        }

        return foundParts;
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

    private static class Ops {
        private final Function<Part, Result> check;

        public Ops(Function<Part, Result> check) {
            this.check = check;
        }
    }

    private static class Workflow {
        private final List<Ops> ops;

        public Workflow(List<Ops> ops) {
            this.ops = ops;
        }
    }

    private static class Result {
        private final Part leftPart;
        private final Part rightPart;
        private final String rightString;

        public Result(Part leftPart, Part rightPart, String rightString) {
            this.leftPart = leftPart;
            this.rightPart = rightPart;
            this.rightString = rightString;
        }
    }

    private static class Part {
        private final long xS, xE, mS, mE, aS, aE, sS, sE;

        public Part(long xS, long xE, long mS, long mE, long aS, long aE, long sS, long sE) {
            this.xS = xS;
            this.xE = xE;
            this.mS = mS;
            this.mE = mE;
            this.aS = aS;
            this.aE = aE;
            this.sS = sS;
            this.sE = sE;
        }

        public Part splitX(int value, boolean greaterThan) {
            if (greaterThan) {
                return new Part(value + 1, xE, mS, mE, aS, aE, sS, sE);
            } else {
                return new Part(xS, value - 1, mS, mE, aS, aE, sS, sE);
            }
        }

        public Part splitM(int value, boolean greaterThan) {
            if (greaterThan) {
                return new Part(xS, xE, value + 1, mE, aS, aE, sS, sE);
            } else {
                return new Part(xS, xE, mS, value - 1, aS, aE, sS, sE);
            }
        }

        public Part splitA(int value, boolean greaterThan) {
            if (greaterThan) {
                return new Part(xS, xE, mS, mE, value + 1, aE, sS, sE);
            } else {
                return new Part(xS, xE, mS, mE, aS, value - 1, sS, sE);
            }
        }

        public Part splitS(int value, boolean greaterThan) {
            if (greaterThan) {
                return new Part(xS, xE, mS, mE, aS, aE, value + 1, sE);
            } else {
                return new Part(xS, xE, mS, mE, aS, aE, sS, value - 1);
            }
        }

        public long calculateVolume() {
            return (xE - xS + 1) * (mE - mS + 1) * (aE - aS + 1) * (sE - sS + 1);
        }
    }
}
