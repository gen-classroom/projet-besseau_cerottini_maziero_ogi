package ch.heig_vd.app;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;


public class BuildTest {
    static String root = "./buildTest";
    static String directoryPath = root +"/mon/site";
    static PrintStream original = System.err;
    @BeforeClass
    public static void setup() {
        File directoryTest = new File(directoryPath);
        directoryTest.mkdirs();
    }

    private static void createConfig(String path) throws IOException {
        File jsonConfig = new File(path+"/config.json");
        BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonConfig));
        jsonWriter.write("{\n" +
                "\"title\":\"a\",\n" +
                "\"b\":\"c\"}");
        jsonWriter.flush();
        jsonWriter.close();
    }

    @Test
    public void directoryBuildCreated() throws IOException {
        String path = directoryPath+"/buildTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        File buildDirectory = new File(path + "/build");
        createConfig(path);
        new CommandLine(new Main()).execute( "build", path);
        assertTrue(buildDirectory.exists());
    }

    @Test
    public void configNotCopied() throws IOException {
        String path = directoryPath+"/configTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);
        File buildDirectory = new File(path+"/build"); //build new directory

        new CommandLine(new Main()).execute( "build", path);

        String[] listOfFiles = buildDirectory.list();
        assertEquals(listOfFiles.length, 0);
    }

    @Test
    public void buildNotInBuildDirectory() throws IOException {
        String path = directoryPath+"/buildNotInBuild";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);

        File buildDirectory = new File(path + "/build"); //build new directory

        new CommandLine(new Main()).execute( "build", path);

        String[] listOfFiles = buildDirectory.list();
        for (String file : listOfFiles) {
            assertFalse(file.contains("build"));
        }
    }

    @Test
    public void FilesCopied() throws IOException {
        String path = directoryPath+"/testCopy";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);

        File newDirectory = new File(directoryTest.getPath() + "/dossier");
        newDirectory.mkdirs();
        File image = new File(newDirectory.getPath() + "/image.png");
        image.createNewFile();

        File input = new File(directoryTest.getPath()+"/input.md");
        FileWriter writer = new FileWriter(input);
        String inputContent = "titre:metaTitle\n" +
                "auteur:metaAuthor\n" +
                "date:metaDate\n" +
                "---\n" +
                "This is *Sparta*";
        writer.write(inputContent);
        writer.close();

        File buildDirectory = new File( path + "/build");

        new CommandLine(new Main()).execute( "build", directoryTest.getPath());

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(Objects.requireNonNull(directoryTest.list())));
        ArrayList<String> listBuild = new ArrayList<String>(Arrays.asList(Objects.requireNonNull(buildDirectory.list())));

        for (String file : list) {
            if(file.equals("config.json") || file.equals("build")){
                assertFalse(listBuild.contains(file));
            }else{
                if (file.substring(file.length() - 3).equals(".md")){
                    assertTrue(listBuild.contains(file.substring(0, file.length()-2) +"html"));
                }else{
                    assertTrue(listBuild.contains(file));
                }

            }
        }

        File buildDirectory2 = new File(buildDirectory.getPath() + "/dossier");
        File directoryTest2 = new File(directoryTest.getPath() + "/dossier");
        ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(Objects.requireNonNull(directoryTest2.list())));
        ArrayList<String> listBuild2 = new ArrayList<String>(Arrays.asList(Objects.requireNonNull(buildDirectory2.list())));

        for (String file : list2){
            assertTrue(listBuild2.contains(file));
        }
    }

    @Test
    public void buildShoulThrowWhenNoConfigIsPresent() throws IOException {
        String path = directoryPath+"/noConfigTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        File buildDirectory = new File(path+"/build"); //build new directory
        // Suppress warning
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));
        assertEquals(2, new CommandLine(new Main()).execute( "build", path));
        System.setErr(original);
    }

    @AfterClass
    public static void cleanAll() throws IOException {
        System.setErr(original);
        Path path = Paths.get(root).normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());

    }


}
