package ch.heig_vd.app.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public static ArrayList<Metadata> extractMetadata(File mdFile) throws RuntimeException {
        // Checks file validity
        if (!isFileValid(mdFile))
            throw new RuntimeException("Invalid input file");

        // Local vars
        ArrayList<Metadata> outputMeta = new ArrayList<>();

        // Reads the file
        BufferedReader reader = null;
        String readLine;
        try {
            reader = new BufferedReader(new FileReader(mdFile));
            readLine = reader.readLine();

            // Reads the file lines
            while (readLine != null && !readLine.equals("---")) {
                outputMeta.add(parseMetaLine(readLine));
                readLine = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Returns the parsed metadata
        return outputMeta;
    }

    public static String extractMarkdownContent(File mdFile) throws RuntimeException {
        // Checks file validity
        if (!isFileValid(mdFile))
            throw new RuntimeException("Invalid input file");

        // Local vars
        StringBuilder output = new StringBuilder();
        boolean beginRead = false;

        try {
            // Reads the given file
            BufferedReader reader = new BufferedReader(new FileReader(mdFile));

            String line = reader.readLine();
            while (line != null) {
                // Stores the content line
                if (beginRead) output.append(line + System.lineSeparator());

                // Starts to read
                if (line.equals("---")) beginRead = true;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // returns the extracted content
        return output.toString();
    }
}
