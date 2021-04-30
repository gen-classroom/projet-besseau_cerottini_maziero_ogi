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
    private final File templateFolder;
    private final ArrayList<Metadata> configMeta;
    private String pageTitle;

    public Converter(File jsonMetadata, File templateDirectory) {
        templateFolder = templateDirectory;
        configMeta = new ArrayList<>();

        // Parses the json config metadata
        try {
            JsonParser.parse(jsonMetadata, (field, value) -> configMeta.add(new Metadata(field, value)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markdownToHTML(File mdFile, String ouputPath) {
        // Checks file validity
        if (mdFile.isDirectory())
            throw new RuntimeException("File cannot be a directory");

        String fileName = mdFile.getName();
        if (!FilenameUtils.getExtension(fileName).equals("md"))
            throw new RuntimeException("File extension must be .md");

        // Output variables
        ArrayList<Metadata> mdMeta = new ArrayList<>();
        String mdContent = "";

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

        /* Inits html data
        htmlOutput.append("<!DOCTYPE html>\n");
        htmlOutput.append("<html>\n");
        htmlOutput.append("<head>\n");

        // Adds the metadata to the html output
        htmlOutput.append("<title>").append(pageTitle).append("</title>\n");

        for (Metadata meta : configMeta)
            htmlOutput.append(generateHtmlMeta(meta)).append("\n");

        for (Metadata meta : mdMeta)
            htmlOutput.append(generateHtmlMeta(meta)).append("\n");

        // Adds the markdown content to html file
        htmlOutput.append("</head>\n<body>\n");

        // Parses the markdown
        Parser parser = Parser.builder().build();
        Node document = parser.parse(mdContent.toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();*/

        // Gets the output field name
        fileName = FilenameUtils.getBaseName(fileName);

        // Generates the output content with template
        String outputHtml = "";
        try {
            TemplateInterpreter interpreter = new TemplateInterpreter(templateFolder);
            outputHtml = interpreter.generate(configMeta, mdMeta, mdContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates and writes in the output
        try {
            File output = new File(ouputPath +"/"+ fileName + ".html");
            FileWriter outWriter = new FileWriter(output);

            // Writes to final html file
            outWriter.write(outputHtml);
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
