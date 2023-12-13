package tasks;

import tools.InternetParser;
import tools.LineUtils;

import java.util.Arrays;
import java.util.List;

import static tools.LineUtils.split;

public class Seven {

    public static final String testData = """         
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
                     """;


    private static class Play implements Comparable<Play> {

        private int bid;
        private String handString;

        private int handStrength = 0;
        private int[] hand = new int[5];

        public Play(String hand, int bid) {
            this.bid = bid;
            handString = hand;
            int[] cardBuckets = new int[12];
            int jokerCount = 0;
            for (int i = 0; i < 5; i++) {
                this.hand[i] = convertFromChar(hand.charAt(i));
            }
            for (int i = 0; i < 5; i++) {
                int card = this.hand[i];
                if (card > 1) {
                    cardBuckets[card - 2]++;
                } else {
                    jokerCount++;
                }
            }
            int trueMax = Arrays.stream(cardBuckets).max().orElseThrow();
            int jokerMax = trueMax + jokerCount;
            if (jokerMax > 3) {
                handStrength = jokerMax + 2;
            } else if (jokerMax == 3) {
                if (trueMax == 2) {
                    if (Arrays.stream(cardBuckets).filter(i -> i == 2).toArray().length == 2) {
                        handStrength = jokerMax + 2;
                    } else {
                        handStrength = jokerMax + 1;
                    }
                } else {
                    if (Arrays.stream(cardBuckets).anyMatch(i -> i == 2)) {
                        handStrength = jokerMax + 2;
                    } else {
                        handStrength = jokerMax + 1;
                    }
                }
            } else if (jokerMax == 2 && Arrays.stream(cardBuckets).filter(i -> i == 2).toArray().length == 2) {
                handStrength = jokerMax + 1;

            } else {
                handStrength = jokerMax;
            }


        }

        @Override
        public String toString() {
            return handString + ":" + handStrength;
        }

        private int convertFromChar(char c) {
            if (c == 'A') {
                return 13;
            }
            if (c == 'K') {
                return 12;
            }
            if (c == 'Q') {
                return 11;
            }
            if (c == 'J') {
                return 1;
            }
            if (c == 'T') {
                return 10;
            }
            return c - '0';
        }

        @Override
        public int compareTo(Play o) {
            int strengh = basedOnNumber(this.handStrength, o.handStrength);
            if (strengh != 0) return strengh;
            for (int i = 0; i < 5; i++) {
                strengh = basedOnNumber(this.hand[i], o.hand[i]);
                if (strengh != 0) return strengh;
            }
            return 0;
        }

        private int basedOnNumber(int n, int on) {
            if (n > on) {
                return 1;
            }
            if (on > n) {
                return -1;
            }
            return 0;
        }
    }

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(7);
        run(testInput, "5905", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        List<Play> playStream = input.stream().map(s -> {
            String[] split = split(s, " ");
            return new Play(split[0], LineUtils.extractInt(split[1]));
        }).sorted().toList();
        long sum = 0;
        for (int i = 0; i < playStream.size(); i++) {
            Play play = playStream.get(i);
            //System.out.println(play);
            sum += play.bid * (i + 1L);
        }
        String answer = "" + sum;
        showAnswer(answer, expectedOutput, startTime);
    }

    public static void showAnswer(String answer, String expectedOutput, long startTime) {
        System.out.println("-----------------------------------------------------------");
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