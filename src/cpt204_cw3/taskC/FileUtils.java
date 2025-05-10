package cpt204_cw3.taskC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String[] readNamesFromFile(String filename) {
        List<String> names = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);
    }
}
