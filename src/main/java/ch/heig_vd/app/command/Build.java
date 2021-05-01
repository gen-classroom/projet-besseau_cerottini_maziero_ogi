package ch.heig_vd.app.command;

import ch.heig_vd.app.converter.Converter;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;


@CommandLine.Command(name = "build",
        exitCodeOnExecutionException = 2,
        description = "Build a static site")
public class Build implements Runnable {
    @CommandLine.Parameters(description = "Path to site to build. (Must contain a config.json file)")
    String filePath;
    Converter converter;

    public void run() {
        Path path = Paths.get(filePath).normalize().toAbsolutePath();
        File filesDirectory = new File(path.toString()); //get all directory from there
        if(!filesDirectory.exists()){
            throw new RuntimeException("Directory does not exist");
        }

        File configFile = new File(path+"/config.json");
        if (!configFile.exists()){
            throw new RuntimeException("Config file does not exist");
        }

        File buildDirectory = new File(path + "/build"); //build new directory
        buildDirectory.mkdir();
        File templateFolder = new File(path + "/template");
        converter = new Converter(configFile, templateFolder);

        try {
            explore(filesDirectory, buildDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occured during the build phase. "+e.getMessage());
        }
    }

    //Get all the files and directories
    void explore(File filesDirectory, File buildDirectory) throws IOException {

        Path path = Paths.get(filePath).normalize().toAbsolutePath();

        File[] listOfFiles = filesDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileName = file.getName();
                if (fileName.contains(".md")) {//MD files become HTML files
                    converter.markdownToHTML(file, buildDirectory.toString());
                } else if (!fileName.contains("config") && !file.isDirectory()) {
                    File newDirectory = new File(buildDirectory + "/" + fileName);
                    FileUtils.copyFile(file, newDirectory);
                } else if (file.isDirectory() && !fileName.contains("build") && !fileName.contains("template")) {
                    File newDirectory = new File(buildDirectory + "/" + fileName); //build new directory
                    newDirectory.mkdir();
                    explore(file, newDirectory);
                }
            }
        }
    }
}