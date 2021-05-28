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

/**
 * Command to compile the source into html file for the static site.
 * Arguments must contain path and optionally a an option to enable the file watcher
 *
 * @author Maziero Marco
 * @author Besseau Léonard
 * @author Cerottini Alexandra
 */
@CommandLine.Command(name = "build",
        exitCodeOnExecutionException = 2,
        description = "Build a static site")
public class Build implements Runnable {
    @CommandLine.Parameters(description = "Path to site to build. (Must contain a config.json file)")
    String filePath;
    @CommandLine.Option(names = {"-w", "--watcher"}, paramLabel = "Watcher", description = "Enable file watcher to automate")
    boolean watcher;
    private Converter converter;

    /**
     * Main entry point for the build command.
     * The folder given needs to exists and must contain a config.json file and a template folder.
     */
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
            enableFileWatcher(path);
        }
    }

    /**
     * Enable the file watcher on a path.
     * If the modified file is a .md file only this file will be reconverted. Otherwise all files will be reconverted.
     *
     * @param path The path to watch
     */
    public void enableFileWatcher(Path path) {
        try {
            String buildDirectory = path + "/build";
            new FileWatcher(path, (name, path1) -> {
                if (FilenameUtils.getExtension(path1.toString()).equals("md")) {
                    if (!name.equals("ENTRY_DELETE")) {
                        converter.markdownToHTML(path1.toFile(), buildDirectory);
                    }
                } else {
                    try {
                        explore(path.toFile(), new File(buildDirectory));
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

    /**
     * Recursively explore all folders in a given directory to convert the file.
     * Does not convert file if the folder is named *build* or *template*
     *
     * @param filesDirectory The folder to explore or the file to convert.
     * @param buildDirectory The build directory to place the newly created file.
     * @throws IOException If an IO exception occurs while converting the file.
     */
    void explore(File filesDirectory, File buildDirectory) throws IOException {

        File[] listOfFiles = filesDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileName = file.getName();
                if (file.exists()) {
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