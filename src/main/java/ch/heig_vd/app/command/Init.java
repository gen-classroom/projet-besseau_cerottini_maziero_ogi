package ch.heig_vd.app.command;

import ch.heig_vd.app.generator.ConfigGenerator;
import ch.heig_vd.app.generator.TemplateGenerator;
import picocli.CommandLine;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


// Class defining the init command used to create the website folders as well as an example of config.json and index.md file
@CommandLine.Command(name = "init",
        description = "Initialize a static site directory",
        exitCodeOnExecutionException = 2)
public class Init implements Runnable {

    @CommandLine.Parameters(description = "Path to site to init.")
    String filePath; // Picocli puts the argument after init into this variable

    public void run() {

        // Normalize the path to remove any "//" or ".." inserted by mistake
        Path path = Paths.get(filePath).normalize().toAbsolutePath();

        File dir = new File(path + "/template");

        // Creates the tree structure if it is not already created
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // Creating the json file
            FileWriter jsonFile = new FileWriter(path + "/config.json");
            jsonFile.write(ConfigGenerator.generateJsonMeta());
            jsonFile.close();
            System.out.println("Json file created");

            // Creating the Markdown file
            FileWriter mdFile = new FileWriter(path + "/index.md");
            mdFile.write(TemplateGenerator.generateIndex());
            mdFile.close();
            System.out.println("Markdown file created");

            // Creating the default HTML page
            FileWriter defaultHTML = new FileWriter(dir.getPath() + "/default.html");
            defaultHTML.write(TemplateGenerator.generateTemplate());
            defaultHTML.close();
            System.out.println("Default HTML file created");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
