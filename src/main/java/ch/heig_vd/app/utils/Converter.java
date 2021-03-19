package ch.heig_vd.app.utils;
import java.io.BufferedReader;
import java.io.*;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Converter {
    public static void MarkdownToHTML(File mdFile, String ouputPath) {
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

            // Parses the markdon
            Parser parser = Parser.builder().build();
            Node document = parser.parse(mdText.toString());
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            // Gets the output fiel name
            String fileName = mdFile.getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1) fileName = fileName.substring(0, dotIndex);

            // Creates and writes in the output
            File output = new File(ouputPath + mdFile.getName() + fileName);
            FileWriter outWriter = new FileWriter(output);
            outWriter.write(renderer.render(document));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
