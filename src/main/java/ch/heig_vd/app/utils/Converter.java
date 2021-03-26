package ch.heig_vd.app.utils;
import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Converter {
    // Atributes
    private ArrayList<Metadata> configMeta;

    public Converter(File jsonMetadata) {}

    public static void MarkdownToHTML(File mdFile, String ouputPath) {
        // Checks file validity
        if (mdFile.isDirectory())
            throw new RuntimeException("File cannot be a directory");

        String fileName = mdFile.getName();
        if (!FilenameUtils.getExtension(fileName).equals("md"))
            throw new RuntimeException("File extension must be .md");

        // Output variables
        ArrayList<Metadata> mdMeta;
        String mdContent;

        // Retrieves the metas inside md file
        try {
            mdMeta = PageParser.extractMetadata(mdFile);
        } catch (RuntimeException e) {
            System.err.println("File " +
                    mdFile.getName() +
                    " could not be parsed and was not added to destination\n" +
                    "Error : " + e.getMessage());
        }

        // Extracts md content
        try {
            mdContent = PageParser.extractMarkdownContent(mdFile);
        } catch (RuntimeException e) {
            System.err.println("Content could not be parsed in file " + mdFile.getName());
        }

        // Adds
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(mdFile));
            StringBuilder mdText = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                mdText.append(line);
                line = reader.readLine();
            }
            reader.close();

            // Parses the markdown
            Parser parser = Parser.builder().build();
            Node document = parser.parse(mdText.toString());
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            // Gets the output field name
            fileName = FilenameUtils.getBaseName(fileName);

            // Creates and writes in the output
            File output = new File(ouputPath + fileName + ".html");
            FileWriter outWriter = new FileWriter(output);
            outWriter.write(renderer.render(document));
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateHtmlMeta(Metadata meta) {
        return "<meta name=\"" + meta.getName() + "\" content=\"" + meta.getContent() + "\">";
    }
}
