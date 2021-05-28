package ch.heig_vd.app.converter;
import java.io.*;
import java.util.ArrayList;

import ch.heig_vd.app.converter.interpreter.TemplateInterpreter;
import ch.heig_vd.app.converter.parser.JsonParser;
import ch.heig_vd.app.converter.parser.PageParser;
import ch.heig_vd.app.converter.utils.Metadata;
import org.apache.commons.io.FilenameUtils;

/**
 * Converts a markdown file into a html output file (uses the parser classes)
 * @author Marco Maziero
 * @author Besseau Léonard
 */
public class Converter {
    private final ArrayList<Metadata> configMeta;
    TemplateInterpreter interpreter;

    /**
     * Initlizes the converter with a given json metadata file and a templates directory
     * @param jsonMetadata The json metadata to apply to each converted file
     * @param templateDirectory The directory containing all the templates used to convert the files
     */
    public Converter(File jsonMetadata, File templateDirectory) {
        configMeta = new ArrayList<>();
        try {
            interpreter = new TemplateInterpreter(templateDirectory);
        }catch (IOException e){
            throw new RuntimeException("Could not create template interpreter. "+e);
        }

        // Parses the json config metadata
        try {
            JsonParser.parse(jsonMetadata, (field, value) -> configMeta.add(new Metadata(field, value)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a given markdown file into an html output file
     * Injects the json metadata and uses a template to create the html output
     * @param mdFile The markdown file to convert
     * @param ouputPath The ouput path of the new html file
     */
    public void markdownToHTML(File mdFile, String ouputPath) {
        // Checks file validity
        if (mdFile.isDirectory())
            throw new RuntimeException("File cannot be a directory");

        String fileName = mdFile.getName();
        if (!FilenameUtils.getExtension(fileName).equals("md"))
            throw new RuntimeException("File extension must be .md");

        // Output variables
        ArrayList<Metadata> mdMeta = new ArrayList<>();
        String mdContent = PageParser.extractAll(mdFile, mdMeta);

        // Gets the output field name
        fileName = FilenameUtils.getBaseName(fileName);

        // Generates the output content with template
        String outputHtml;
        try {
            outputHtml = interpreter.generate(configMeta, mdMeta, mdContent);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not insert data into template. "+e.getMessage());
        }

        // Creates and writes in the output
        File output = new File(ouputPath +"/"+ fileName + ".html");
        try(FileWriter outWriter = new FileWriter(output)) {
            outWriter.write(outputHtml);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while writing file. "+e.getMessage());
        }
    }
}
