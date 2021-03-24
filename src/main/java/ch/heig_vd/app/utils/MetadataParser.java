package ch.heig_vd.app.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.*;

public class MetadataParser {
    private static String parseMetaLine(String head, String line) {
        if (line.substring(0, head.length()).equals(head))
            return line.substring(head.length());
        else
            throw new RuntimeException("Incorrect " + head +  " line");
    }

    public static Metadata exctractMetadata(File mdFile) throws RuntimeException {
        // Checks file validity
        if (mdFile.isDirectory())
            throw new RuntimeException("File cannot be a directory");

        String filename = mdFile.getName();

        if (!FilenameUtils.getExtension(filename).equals("md"))
            throw new RuntimeException("File extension must be .md");

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

            // Checks end of metadata line
            if (!metaLines[nbMetaLines - 1].equals("---"))
                throw new RuntimeException("Missing end of metadata line");

            // Parses the extracted lines
            outputMeta.title = parseMetaLine("titre:", metaLines[0]);
            outputMeta.author = parseMetaLine("auteur:", metaLines[1]);
            outputMeta.date = parseMetaLine("date:", metaLines[2]);

            // creates temp file to write in
            File tmpFile = new File(FilenameUtils.getPath(mdFile.getPath()) + "/tmp" + mdFile.getName());
            FileWriter writer = new FileWriter(tmpFile);

            // writes in tmp file
            String newLine = reader.readLine();
            while (newLine != null) {
                writer.write(newLine);
                newLine = reader.readLine();
            }

            // Closes streams
            writer.flush();
            writer.close();
            reader.close();

            // Deletes old file and renames tmp
            mdFile.delete();
            tmpFile.renameTo(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Returns the parsed metadata
        return outputMeta;
    }
}
