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
        File buildDirectory = new File(directory.getPath() + path + "/build");


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
        File configFile = new File(directoryTest.getPath() + "/config.yaml");
        configFile.createNewFile();

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
        File configFile = new File(directoryTest.getPath() + "/config.yaml");
        configFile.createNewFile();
        File newDirectory = new File(directoryTest.getPath() + "/dossier");
        newDirectory.mkdirs();
        File image = new File(newDirectory.getPath() + "/image.png");
        image.createNewFile();

        File buildDirectory = new File(directory.getPath() + path + "/build");

        new CommandLine(new Main()).execute("statique", "build", path);

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(directoryTest.list()));
        ArrayList<String> listBuild = new ArrayList<String>(Arrays.asList(buildDirectory.list()));

        for (String file : list) {
            if((!file.contains("config")) && (!file.contains("build"))){
                assertTrue(listBuild.contains(file));
            }
        }

        File buildDirectory2 = new File(buildDirectory.getPath() + "/dossier");
        File directoryTest2 = new File(directoryTest.getPath() + "/dossier");
        ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(directoryTest2.list()));
        ArrayList<String> listBuild2 = new ArrayList<String>(Arrays.asList(buildDirectory2.list()));

        for (String file : list2){
            if((!file.contains("config")) && (!file.contains("build"))){
                assertTrue(listBuild2.contains(file));
            }
        }
    }
}
