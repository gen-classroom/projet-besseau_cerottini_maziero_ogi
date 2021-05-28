package ch.heig_vd.app.command;

import ch.heig_vd.app.converter.Converter;
import ch.heig_vd.app.fileWatcher.FileWatcher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@CommandLine.Command(name = "build",
        exitCodeOnExecutionException = 2,
        description = "Build a static site")
public class Build implements Runnable {
    @CommandLine.Parameters(description = "Path to site to build. (Must contain a config.json file)")
    String filePath;
    @CommandLine.Option(names = {"-w", "--watcher"}, paramLabel = "Watcher", description = "Enable file watcher to automate")
    boolean watcher;
    Converter converter;

    public void run() {
        Path path = Paths.get(filePath).normalize().toAbsolutePath();
        File filesDirectory = new File(path.toString()); //get all directory from there
        if (!filesDirectory.exists()) {
            throw new RuntimeException("Directory does not exist");
        }

        File configFile = new File(path + "/config.json");
        if (!configFile.exists()) {
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
            throw new RuntimeException("An error occurred during the build phase. " + e.getMessage());
        }

        if (watcher) {
            try {
                new FileWatcher(path, (name, path1) -> {
                    if (FilenameUtils.getExtension(path1.toString()).equals("md")) {
                        if(!name.equals("ENTRY_DELETE")){
                            converter.markdownToHTML(path1.toFile(), buildDirectory.toString());
                        }
                    } else {
                        try {
                            explore(filesDirectory, buildDirectory);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("An error occurred during the build phase. " + e.getMessage());
                        }
                    }
                });
            } catch (IOException e) {
                System.err.println("File watcher error\n" + e.getMessage());
            }
        }
    }

    //Get all the files and directories
    void explore(File filesDirectory, File buildDirectory) throws IOException {

        File[] listOfFiles = filesDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileName = file.getName();
                if (file.exists()){
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
}