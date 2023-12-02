package tools;

public class LineUtils {

    public static int extractInt(String s) {
        return Integer.parseInt(s.replace("[^0-9]", ""));
    }

    public static double extractDouble(String s) {
        return Double.parseDouble(s.replace("[^0-9.]", ""));
    }

    public static int extractIntNegative(String s) {
        return Integer.parseInt(s.replace("[^0-9-]", ""));
    }

    public static double extractDoubleNegative(String s) {
        return Double.parseDouble(s.replace("[^0-9.-]", ""));
    }

    public static String[] split(String string, String splitter) {
        String[] split = string.split(splitter);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }

}
