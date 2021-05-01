package ch.heig_vd.app.converter.parser;

import ch.heig_vd.app.converter.utils.Metadata;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Marco Maziero
 * @author Besseau Léonard
 */
public class PageParser {
    private static boolean isFileValid(File file) {
        // Checks file validity
        if (file.isDirectory())
            return false;

        String filename = file.getName();
        return FilenameUtils.getExtension(filename).equals("md");
    }

    private static Metadata parseMetaLine(String line) {
        String[] tokens = line.split(":");
        if (tokens.length < 2) throw new RuntimeException("Incorrect " + line +  " metadata line\nCheck end of metadata separator \"---\"");
        return new Metadata(tokens[0], tokens[1]);
    }

    /**
     * Extract content and metaData from file
     * @param file the file to use
     * @param outputMetaData the array to store the metadata in
     * @return the content of the file
     */
    public static String extractAll(File file, ArrayList<Metadata> outputMetaData){
        if (!isFileValid(file)) {
            throw new RuntimeException("Invalid input file");
        }

        StringBuilder output = new StringBuilder();
        // Reads the file
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            line = reader.readLine();

            // Reads metaData
            while (line != null && !line.equals("---")) {
                outputMetaData.add(parseMetaLine(line));
                line = reader.readLine();
            }
            // Needed to avoid adding the separator into the content
            if(line != null){
                line = reader.readLine();
                // reads content
                while (line != null) {
                    // Stores the content line
                    output.append(line).append(System.lineSeparator());

                    line = reader.readLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while reading file "+file+". "+e.getMessage());
        }
        return output.toString();
    }
}
