package ch.heig_vd.app.command;

import org.apache.commons.io.FileUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command to clean the build folder of a static site
 *
 * @author Besseau Léonard
 */
@CommandLine.Command(name = "clean",
        description = "Clean a static site",
        exitCodeOnExecutionException = 2)
public class Clean implements Runnable {
    @CommandLine.Parameters(description = "Path to site to clean. (Must contain a build folder)")
    String filePath;

    /**
     * Clean the build folder of a static site
     *
     * @throws IllegalArgumentException if the build directory does not exist in the given path
     */
    public void run() {
        try {
            Path path = Paths.get(filePath, "/build").normalize().toAbsolutePath();
            if (!path.toFile().exists()) {
                throw new IllegalArgumentException("Directory does not exists");
            }
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while deleting the file. " + e.getMessage());
        }
    }
}