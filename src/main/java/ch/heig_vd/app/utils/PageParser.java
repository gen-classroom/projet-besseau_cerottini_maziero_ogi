package ch.heig_vd.app.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.*;

public class PageParser {
    private static boolean isFileValid(File file) {
        // Checks file validity
        if (file.isDirectory())
            return false;

        String filename = file.getName();
        return FilenameUtils.getExtension(filename).equals("md");
    }

    private static String parseMetaLine(String head, String line) {
        if (line.substring(0, head.length()).equals(head))
            return line.substring(head.length());
        else
            throw new RuntimeException("Incorrect " + head +  " line");
    }

    public static Metadata exctractMetadata(File mdFile) throws RuntimeException {
        // Checks file validity
        if (!isFileValid(mdFile))
            throw new RuntimeException("Invalid input file");

        // Local vars
        int nbMetaLines = 4;
        String[] metaLines = new String[nbMetaLines];
        Metadata outputMeta = new Metadata();

        // Reads the file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(mdFile));

            // Reads the title line
            for (int i = 0; i < metaLines.length; ++i) {
                metaLines[i] = reader.readLine();
                if (metaLines[i] == null)
                    throw new RuntimeException("Metadata are incomplete");
            }
            reader.close();

            // Checks end of metadata line
            if (!metaLines[nbMetaLines - 1].equals("---"))
                throw new RuntimeException("Missing end of metadata line");

            // Parses the extracted lines
            outputMeta.title = parseMetaLine("titre:", metaLines[0]);
            outputMeta.author = parseMetaLine("auteur:", metaLines[1]);
            outputMeta.date = parseMetaLine("date:", metaLines[2]);
        } catch (IOException e) {
            e.printStackTrace();
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
                if (beginRead) output.append(line);

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
