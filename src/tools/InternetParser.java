package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternetParser {

    public static List<String> getInput(int day) {
        String fileName = "resources/task/" + day + ".txt";
        if (new File(fileName).isFile()) {
            return new FileParser().parseFile(fileName);
        }
        try {
            String value = new FileParser().parseFile("resources/cookie.txt").get(0);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://adventofcode.com/2024/day/" + day + "/input"))
                    .GET()
                    .header("cookie", value)
                    .build();
            List<String> list = Arrays.asList(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body().split("\n"));
            if (list.size() == 1) {
                var input = list.get(0);
                if (input.equals("Please don't repeatedly request this endpoint before it unlocks! The calendar countdown is synchronized with the server time; the link will be enabled on the calendar the instant this puzzle becomes available.")) {
                    System.out.println("Wrong day");
                    return Collections.emptyList();
                }
            }
            write(String.join("\n", list), fileName);
            return list;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Use FileWriter when number of write operations are less
     *
     * @param data
     */
    private static void write(String data, String fileName) {
        File file = new File(fileName);
        try (FileWriter fr = new FileWriter(file)) {
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getLeader(int year, int day) {
        try {
            String value = new FileParser().parseFile("resources/cookie.txt").get(0);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://adventofcode.com/20" + year + "/leaderboard/day/" + day))
                    .GET()
                    .header("cookie", value)
                    .build();
            List<String> list = Arrays.asList(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body().split("\n"));
            return list;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }
}
