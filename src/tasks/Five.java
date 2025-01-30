package tasks;

import tools.InternetParser;

import java.util.*;

public class Five {

    public static final String testData = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
                        
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """;

    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(5);
        run(testInput, "123", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        long sum = 0;
        Map<Integer, List<Integer>> orders = new HashMap<>();
        List<List<Integer>> books = new ArrayList<>();
        boolean foundAllOrders = false;
        for (String s : input){
            if(!foundAllOrders){
                if(s.isBlank()){
                    foundAllOrders = true;
                } else {
                    String[] o = s.split("\\|");
                    List<Integer> exist = orders.computeIfAbsent(Integer.parseInt(o[0]), unused ->  new ArrayList<>());
                    exist.add(Integer.parseInt(o[1]));
                }
            } else {
                books.add(new ArrayList<>(Arrays.stream(s.split(",")).map(Integer::parseInt).toList()));
            }
        }
        for (List<Integer> book : books){
            if(!isValid(book, orders)){
                book = reorderBook(book, orders);
                sum += book.get(book.size()/2);
            }
        }
        showAnswer(String.valueOf(sum), expectedOutput, startTime);
    }

    private static List<Integer> reorderBook(List<Integer> book, Map<Integer, List<Integer>> orders) {
        List<Integer> found = new ArrayList<>();
        for (int i = 0; i < book.size(); i++){
            int page = book.get(i);
            List<Integer> integers = orders.getOrDefault(page, new ArrayList<>());
            for (Integer after : integers){
                int index = found.indexOf(after);
                if (index != -1){
                    book.set(index, page);
                    book.set(i, after);
                    return reorderBook(book, orders);

                }
            }
            found.add(page);
        }
        return book;

    }

    private static boolean isValid(List<Integer> book, Map<Integer, List<Integer>> orders) {
        Set<Integer> found = new HashSet<>();
        for (Integer page : book){
            List<Integer> integers = orders.getOrDefault(page, new ArrayList<>());
            for (Integer after : integers){
                if (found.contains(after)){
                    return false;
                }
            }
            found.add(page);
        }
        return true;
    }

    private record Order(int before, int after){}

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
