package ch.heig_vd.app.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


// Class defining the init command used to create the website folders as well as an example of config.json and index.md file
@CommandLine.Command(name = "init", exitCodeOnExecutionException = 2)
public class Init implements Runnable {

    @CommandLine.Parameters(index = "0")
    String filePath; // Picocli puts the argument after init into this variable

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    Date date = new Date();

    public void run() {

        // Normalize the path to remove any "//" or ".." inserted by mistake
        Path path = Paths.get(filePath).normalize().toAbsolutePath();

        File dir = new File(path.toString());

        // Creates the tree structure if it is not already created
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Creating the structure of the json file
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("title", "Title");
        rootNode.put("description", "A poisonous atmosphere");
        rootNode.put("domain", "poisonous.atmosphere.org");

        try {
            //Creating the json file
            FileWriter jsonFile = new FileWriter(path.toString() + "/config.json");
            jsonFile.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
            jsonFile.close();
            System.out.println("JSON file created: " + mapper);

            // Creating the Markdown file
            FileWriter mdFile = new FileWriter(path.toString() + "/index.md");
            mdFile.write("# My too classy site !\n");
            mdFile.write("Author : a huge bg\n");
            mdFile.write("Date : " + dateFormat.format(date));
            mdFile.close();
            System.out.println("Markdown file created");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
