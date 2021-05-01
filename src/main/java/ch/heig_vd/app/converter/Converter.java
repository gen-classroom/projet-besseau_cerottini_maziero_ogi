package ch.heig_vd.app.converter;
import java.io.*;
import java.util.ArrayList;

import ch.heig_vd.app.converter.interpreter.TemplateInterpreter;
import ch.heig_vd.app.converter.parser.JsonParser;
import ch.heig_vd.app.converter.parser.PageParser;
import ch.heig_vd.app.converter.utils.Metadata;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Marco Maziero
 * @author Besseau Léonard
 */
public class Converter {
    private final ArrayList<Metadata> configMeta;
    TemplateInterpreter interpreter;

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
