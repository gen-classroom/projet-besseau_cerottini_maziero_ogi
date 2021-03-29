package ch.heig_vd.app.command;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

@CommandLine.Command(name = "clean",
        description = "Clean a static site",
        exitCodeOnExecutionException = 2)
public class Clean implements Runnable {
    @CommandLine.Parameters(description = "Path to site to clean. (Must contain a build folder")
    String filePath;

    public void run() {
        try {
            Path path = Paths.get(filePath, "/build").normalize().toAbsolutePath();
            System.out.println(path);
            if (!path.toFile().exists()) {
                throw new IllegalArgumentException("Directory does not exists");
            }
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}