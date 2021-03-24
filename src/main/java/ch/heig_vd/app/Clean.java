package ch.heig_vd.app;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

@CommandLine.Command(name = "clean", exitCodeOnExecutionException = 2)
class Clean implements Runnable {
    @CommandLine.Parameters(index = "0")
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