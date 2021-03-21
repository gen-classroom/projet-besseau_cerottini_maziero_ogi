package ch.heig_vd.app;

import picocli.CommandLine;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

// Classe définissant la commande Init permettant de créer les dossiers du site web ainsi qu'un exemple de fichier
// config.json et index.md
@CommandLine.Command(name = "Init")
class Init implements Runnable {

    private final static String FILEPATH_JSON = "./mon/site/config.json";
    private final static String FILEPATH_MD = "./mon/site/index.md";

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

    public void run() {

        File dir = new File("./mon/site/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Création de la structure du fichier json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "Titre");
        jsonObject.put("description", "Une ambiance délétère");
        jsonObject.put("domaine", "ambiance.deletere.org");


        try {
            // Instanciation du fichir json
            FileWriter jsonFile = new FileWriter(FILEPATH_JSON);
            jsonFile.write(jsonObject.toJSONString());
            jsonFile.close();
            System.out.println("JSON file created: " + jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Création du fichier Markdown
            FileWriter mdFile = new FileWriter(FILEPATH_MD);
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