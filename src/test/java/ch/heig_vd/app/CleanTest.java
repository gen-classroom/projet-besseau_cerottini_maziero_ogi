package ch.heig_vd.app;

import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CleanTest {


    @Test
    public void cleanShouldDeleteBuildFolderWithRelativePath() throws IOException {
        String path1 = "./mon/site/";
        String path2 = "mon/site/";

        File directory = new File(path1 + "/build");
        directory.mkdirs();
        new CommandLine(new Main()).execute("statique", "clean", path1);
        assertFalse(directory.exists());
        directory = new File(path2 + "/build");
        directory.mkdirs();
        new CommandLine(new Main()).execute("statique", "clean", path2);
        assertFalse(directory.exists());
    }

    @Test
    public void cleanShouldDeleteBuildFolderWithAbsolutePath() throws IOException {
        String path = "/mon/site/";
        String pwd = new File(".").getCanonicalPath();
        File directory = new File(pwd+"/"+path + "/build/");
        directory.delete();
        if (directory.mkdirs()) {
            new CommandLine(new Main()).execute("statique","clean", pwd+"/"+path );
            assertFalse(directory.exists());
        } else {
            System.err.println("Could not test for absolute path");
        }
    }

    @Test
    public void cleanShouldThrowsWhenGivenAnNonExistentPath() throws IOException {
        String path = "/should/not/exists/";
        assertEquals(2, new CommandLine(new Main()).execute("statique", "clean", path));


    }


}
