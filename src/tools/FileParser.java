package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public List<String> parseFile(String filename) {
        try {
            FileInputStream stream = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String strLine;
            ArrayList<String> lines = new ArrayList<String>();
            while ((strLine = reader.readLine()) != null) {
                lines.add(strLine);
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("File cannot be read");
    }
}
