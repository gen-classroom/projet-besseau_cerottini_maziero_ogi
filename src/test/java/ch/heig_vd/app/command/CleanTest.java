package ch.heig_vd.app.command;

import ch.heig_vd.app.Main;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CleanTest {
    static PrintStream original = System.err;
    @Test
    public void cleanShouldDeleteBuildFolderWithRelativePath() throws IOException {
        String path1 = "./test_folder/mon/site/";
        String path2 = "test_folder/mon/site/";

        File directory = new File(path1 + "/build");
        directory.mkdirs();
        new CommandLine(new Main()).execute( "clean", path1);
        assertFalse(directory.exists());
        directory = new File(path2 + "/build");
        directory.mkdirs();
        new CommandLine(new Main()).execute( "clean", path2);
        assertFalse(directory.exists());

    }

    @Test
    public void cleanShouldDeleteBuildFolderWithAbsolutePath() throws IOException {
        String path = "/test_folder/mon/site/";
        String pwd = new File(".").getCanonicalPath();
        File directory = new File(pwd + "/" + path + "/build/");
        directory.delete();
        if (directory.mkdirs()) {
            new CommandLine(new Main()).execute( "clean", pwd + "/" + path);
            assertFalse(directory.exists());
        } else {
            System.err.println("Could not test for absolute path");
        }
    }

    @Test
    public void cleanShouldThrowsWhenGivenAnNonExistentPath() throws IOException {
        String path = "/should/not/exists/";
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));

        assertEquals(2, new CommandLine(new Main()).execute( "clean", path));
        System.setErr(original);
    }

    @AfterClass
    public static void cleanAll() throws IOException {
        System.setErr(original);
        Path path = Paths.get("./test_folder").normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());

    }
}
