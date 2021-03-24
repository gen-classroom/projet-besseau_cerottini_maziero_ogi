package ch.heig_vd.app.utils;
import java.io.BufferedReader;
import java.io.*;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Converter {
    public static void MarkdownToHTML(File mdFile, String ouputPath) {
        // Checks file validity
        if (mdFile.isDirectory())
            throw new RuntimeException("File cannot be a directory");

        String fileName = mdFile.getName();
        String test = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileName.substring(fileName.lastIndexOf(".") + 1).equals("md"))
            throw new RuntimeException("File extension must be .md");

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
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1) fileName = fileName.substring(0, dotIndex);

            // Creates and writes in the output
            File output = new File(ouputPath + fileName + ".html");
            FileWriter outWriter = new FileWriter(output);
            outWriter.write(renderer.render(document));
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
