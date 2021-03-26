package ch.heig_vd.app;

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


// Classe définissant la commande Init permettant de créer les dossiers du site web ainsi qu'un exemple de fichier
// config.json et index.md
@CommandLine.Command(name = "init", exitCodeOnExecutionException = 2)
class Init implements Runnable {

    @CommandLine.Parameters(index = "0")
    String filePath; // Picocli met l'argument venant après init dans cette variable

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    Date date = new Date();

    public void run() {

        // Permet de normaliser le chemin pour retirer les éventuels "//" ou ".." insérés par erreur
        Path path = Paths.get(filePath).normalize().toAbsolutePath();

        File dir = new File(path.toString());

        // Crée l'arborescence si elle n'est pas déjà créée
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Création de la structure du fichier json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("title", "Titre");
        rootNode.put("description", "Une ambiance délétère");
        rootNode.put("domain", "ambiance.deletere.org");

        try {
            // Création du fichier json
            FileWriter jsonFile = new FileWriter(path.toString() + "/config.json");
            jsonFile.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
            jsonFile.close();
            System.out.println("JSON file created: " + mapper);

            // Création du fichier Markdown
            FileWriter mdFile = new FileWriter(path.toString() + "/index.md");
            mdFile.write("# Mon site trop classe !\n");
            mdFile.write("Auteur : un énorme bg\n");
            mdFile.write("Date : " + dateFormat.format(date));
            mdFile.close();
            System.out.println("Markdown file created");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}