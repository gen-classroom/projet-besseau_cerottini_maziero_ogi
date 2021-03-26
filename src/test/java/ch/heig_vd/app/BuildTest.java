package ch.heig_vd.app;

import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

public class BuildTest {

    @Test
    public void directoryBuildCreated() throws IOException {
        File directory = new File(new File(".").getCanonicalPath());
        String path = "/mon/site";
        File directoryTest = new File(directory.getPath() + path);
        directoryTest.mkdirs();

        File buildDirectory = new File(directory.getPath() + path + "/build"); //build new directory

        new CommandLine(new Main()).execute("statique", "build", path);
        assertTrue(buildDirectory.exists());
    }

    @Test
    public void configNotCopied() throws IOException {
        File directory = new File(new File(".").getCanonicalPath());
        String path = "/mon/site";
        File directoryTest = new File(directory.getPath() + path);
        directoryTest.mkdirs();
        File configFile = new File(directoryTest.getPath() + "/config.yaml");
        configFile.createNewFile();

        File buildDirectory = new File(directory.getPath() + path + "/build"); //build new directory

        new CommandLine(new Main()).execute("statique", "build", path);


        String[] listOfFiles = buildDirectory.list();
        for (String file : listOfFiles) {
            assertFalse(file.contains("config"));
        }
    }

    @Test
    public void buildNotInBuildDirectory() throws IOException {
        File directory = new File(new File(".").getCanonicalPath());
        String path = "/mon/site";
        File directoryTest = new File(directory.getPath() + path);
        directoryTest.mkdirs();

        File buildDirectory = new File(directory.getPath() + path + "/build"); //build new directory

        new CommandLine(new Main()).execute("statique", "build", path);

        String[] listOfFiles = buildDirectory.list();
        for (String file : listOfFiles) {
            assertFalse(file.contains("build"));
        }
    }

    @Test
    public void FilesCopied() throws IOException {
        File directory = new File(new File(".").getCanonicalPath());
        String path = "/mon/site";
        File directoryTest = new File(directory.getPath() + path);
        directoryTest.mkdirs();

        File buildDirectory = new File(directory.getPath() + path + "/build");

        new CommandLine(new Main()).execute("statique", "build", path);

        //File directoryTest = new File(directory.getPath() + path);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(directoryTest.list()));
        ArrayList<String> listBuild = new ArrayList<String>(Arrays.asList(buildDirectory.list()));

        for (String file : list) {
            if((!file.contains("config")) && (!file.contains("build"))){
                assertTrue(listBuild.contains(file));
            }

        }

    }

    @AfterAll
    public void cleanUp() {
        File file = new File("testFile.txt");
        File jsonConfig = new File("./conf.json");
        File input = new File("./input.md");
        file.delete();
        jsonConfig.delete();
        input.delete();
    }


}
