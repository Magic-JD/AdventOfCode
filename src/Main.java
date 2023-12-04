
        import tasks.Three;
        import tools.InternetParser;

        import java.util.*;

        import static tools.LineUtils.extractInt;

public class Main {

    public static final String testData = """

            """;


    private static class JoeNumber{
        int number;
        public JoeNumber(int number){
            this.number = number;
        }
    }

    private static Map<String, JoeNumber> map = new HashMap<>();
    private static List<List<Integer>> stars = new ArrayList<>();

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(4);
        run(testInput, "-");
        run(mainInput, "???");
    }

    public static void run(List<String> input, String expectedOutput){
        String answer = "";
        showAnswer(answer, expectedOutput);
    }

    public static void showAnswer(String answer, String expectedOutput){
        if(expectedOutput.equals("???")){
            System.out.println("The actual output is : " + answer);
        } else {
            System.out.println("Current answer = " + answer + ". Expected answer = " + expectedOutput);
            if(answer.equals(expectedOutput)){
                System.out.println("CORRECT");
            } else {
                System.out.println("INCORRECT");
            }
        }
    }

}