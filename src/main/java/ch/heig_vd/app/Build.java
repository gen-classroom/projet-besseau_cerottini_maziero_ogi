package ch.heig_vd.app;

import ch.heig_vd.app.utils.Converter;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;


@CommandLine.Command(name = "build", exitCodeOnExecutionException = 2)
class Build implements Runnable {
    @CommandLine.Parameters(index = "0")
    String filePath;
    Converter converter;

    public void run() {

        Path path = Paths.get(filePath).normalize().toAbsolutePath();

        File buildDirectory = new File(path + "/build"); //build new directory
        buildDirectory.mkdir();
        File filesDirectory = new File(path.toString()); //get all directory from there
        File configFile = new File(path+"/config.json");
        if (!configFile.exists()){
            throw new RuntimeException("Config file does not exist");
        }
        converter = new Converter(configFile);
        try {
            explore(filesDirectory, buildDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get all the files and directories
    void explore(File filesDirectory, File buildDirectory) throws IOException {

        File[] listOfFiles = filesDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileName = file.getName();
                if (fileName.contains(".md")) {//MD files become HTML files
                    converter.markdownToHTML(file, buildDirectory.toString());
                } else if (!fileName.contains("config") && !file.isDirectory()) {
                    File newDirectory = new File(buildDirectory + "/" + fileName);
                    FileUtils.copyFile(file, newDirectory);
                } else if (file.isDirectory() && !fileName.contains("build")) {
                    File newDirectory = new File(buildDirectory + "/" + fileName); //build new directory
                    newDirectory.mkdir();
                    explore(file, newDirectory);
                }
            }
        }
    }
}