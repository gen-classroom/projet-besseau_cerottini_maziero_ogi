package ch.heig_vd.app;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class InitTest {

    @BeforeAll
    public static void invokeCommandLine() {
        new CommandLine(new Main()).execute("statique", "Init");
    }

    @Test
    public void initShouldCreateDirectories() {
        File dir = new File("./mon/site/");
        assertFalse(dir.exists());
    }

    @Test
    public void initShouldCreateJson() {
        File jsonFile = new File("./mon/site/config.json");
        assertFalse(jsonFile.exists());
    }

    @Test
    public void initShouldCreateMd() throws IOException {
        File mdFile = new File("./mon/site/index.md");
        assertFalse(mdFile.exists());
    }
}
