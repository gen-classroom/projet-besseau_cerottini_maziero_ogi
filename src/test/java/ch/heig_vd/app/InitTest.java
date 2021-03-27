package ch.heig_vd.app;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class InitTest {

    @BeforeClass
    public static void invokeCommandLine() {
        new CommandLine(new Main()).execute("statique", "init", "test_folder/mon//site");
    }

    @Test
    public void initShouldCreateDirectories() {
        File dir = new File("test_folder/mon/site/");
        assertTrue(dir.exists());
    }

    @Test
    public void initShouldCreateJson() {
        File jsonFile = new File("test_folder/mon/site/config.json");
        assertTrue(jsonFile.exists());
    }

    @Test
    public void initShouldCreateMd() {
        File mdFile = new File("test_folder/mon/site/index.md");
        assertTrue(mdFile.exists());
    }

    @AfterClass
    public static void cleanAll() throws IOException {
        Path path = Paths.get("./test_folder").normalize().toAbsolutePath();
        System.out.println("Cleaning ALL");
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());

    }
}
