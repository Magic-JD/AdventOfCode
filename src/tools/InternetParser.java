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
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://adventofcode.com/2023/day/" + day + "/input"))
                    .GET()
                    .header("cookie", "_ga=GA1.2.806319161.1701337183; _gid=GA1.2.695923468.1701337183; _ga_MHSNPJKWC7=GS1.2.1701447783.7.1.1701447902.0.0.0; session=53616c7465645f5f34cc841cda1739c98cc90fc08779fc92b360d116370a40f77cc46b9062bffd6dda8eb8b7bdb166b6dac75eb8e0ad2af052ba56d66b988813")
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

}
