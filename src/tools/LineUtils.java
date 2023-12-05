package tools;

import java.util.Arrays;

public class LineUtils {

    public static long extractLong(String s) {
        return Long.parseLong(s.replaceAll("[^0-9]", ""));
    }

    public static int extractInt(String s) {
        return Integer.parseInt(s.replaceAll("[^0-9]", ""));
    }

    public static double extractDouble(String s) {
        return Double.parseDouble(s.replaceAll("[^0-9.]", ""));
    }

    public static int extractIntNegative(String s) {
        return Integer.parseInt(s.replaceAll("[^0-9-]", ""));
    }

    public static double extractDoubleNegative(String s) {
        return Double.parseDouble(s.replaceAll("[^0-9.-]", ""));
    }

    public static String[] split(String string, String regex) { // Must escape out regex characters
        return Arrays.stream(string.split(regex))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);

    }

}
