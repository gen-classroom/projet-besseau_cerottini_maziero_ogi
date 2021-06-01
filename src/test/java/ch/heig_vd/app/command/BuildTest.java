package ch.heig_vd.app.command;

import ch.heig_vd.app.Main;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


public class BuildTest {
    static String root = "./buildTest";
    static String directoryPath = root + "/mon/site";
    static PrintStream original = System.err;

    @BeforeAll
    public static void setup() {
        File directoryTest = new File(directoryPath);
        directoryTest.mkdirs();
    }

    private static void createConfig(String path) throws IOException {
        File jsonConfig = new File(path + "/config.json");
        BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonConfig));
        jsonWriter.write("{\n" +
                "\"title\":\"a\",\n" +
                "\"b\":\"c\"}");
        jsonWriter.flush();
        jsonWriter.close();
    }

    private static void createTemplateDirectory(String path) throws IOException {
        File template = new File(path + "/template");
        template.mkdirs();
        File file1 = new File(path + "/template/default.html");
        PrintWriter writer1 = new PrintWriter(new BufferedWriter(new FileWriter(file1)));
        writer1.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer1.flush();
        writer1.close();
    }

    @Test
    public void directoryBuildCreated() throws IOException {
        String path = directoryPath + "/buildTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        File buildDirectory = new File(path + "/build");
        createConfig(path);
        createTemplateDirectory(path);
        assertEquals(0, new CommandLine(new Main()).execute("build", path));
        assertTrue(buildDirectory.exists());
    }

    @Test
    public void nonExistentDirectoryShouldThrow() throws IOException {
        String path = directoryPath + "/doesNotExist";
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));
        assertEquals(2, new CommandLine(new Main()).execute("build", path));
        System.setErr(original);
    }

    @Test
    public void configNotCopied() throws IOException {
        String path = directoryPath + "/configTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);
        createTemplateDirectory(path);
        File buildDirectory = new File(path + "/build"); //build new directory

        assertEquals(0, new CommandLine(new Main()).execute("build", path));

        String[] listOfFiles = buildDirectory.list();
        assert listOfFiles != null;
        assertEquals(listOfFiles.length, 0);
    }

    @Test
    public void buildNotInBuildDirectory() throws IOException {
        String path = directoryPath + "/buildNotInBuild";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);
        createTemplateDirectory(path);

        File buildDirectory = new File(path + "/build"); //build new directory
        assertEquals(0, new CommandLine(new Main()).execute("build", path));

        String[] listOfFiles = buildDirectory.list();
        for (String file : listOfFiles) {
            assertFalse(file.contains("build"));
        }
    }

    @Test
    public void FilesCopied() throws IOException {
        String path = directoryPath + "/testCopy";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);
        createTemplateDirectory(path);

        File newDirectory = new File(directoryTest.getPath() + "/dossier");
        newDirectory.mkdirs();
        File image = new File(newDirectory.getPath() + "/image.png");
        image.createNewFile();

        File input = new File(directoryTest.getPath() + "/input.md");
        FileWriter writer = new FileWriter(input);
        String inputContent = "titre:metaTitle\n" +
                "auteur:metaAuthor\n" +
                "date:metaDate\n" +
                "---\n" +
                "This is *Sparta*";
        writer.write(inputContent);
        writer.close();

        File buildDirectory = new File(path + "/build");


        assertEquals(0, new CommandLine(new Main()).execute("build", directoryTest.getPath()));
        ArrayList<String> list = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directoryTest.list())));
        ArrayList<String> listBuild = new ArrayList<>(Arrays.asList(Objects.requireNonNull(buildDirectory.list())));

        for (String file : list) {
            if (file.equals("config.json") || file.equals("build") || file.equals("template")) {
                assertFalse(listBuild.contains(file));
            } else {
                if (file.endsWith(".md")) {
                    assertTrue(listBuild.contains(file.substring(0, file.length() - 2) + "html"));
                } else {
                    assertTrue(listBuild.contains(file));
                }

            }
        }

        File buildDirectory2 = new File(buildDirectory.getPath() + "/dossier");
        File directoryTest2 = new File(directoryTest.getPath() + "/dossier");
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directoryTest2.list())));
        ArrayList<String> listBuild2 = new ArrayList<>(Arrays.asList(Objects.requireNonNull(buildDirectory2.list())));

        for (String file : list2) {
            assertTrue(listBuild2.contains(file));
        }
    }

    @Test
    public void checkFileWatcherReloadsFile() throws IOException {
        String path = directoryPath + "/testFileWatcher";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        createConfig(path);
        createTemplateDirectory(path);

        File input = new File(directoryTest.getPath() + "/input.md");
        FileWriter writer = new FileWriter(input);
        String inputContent = "titre:metaTitle\n" +
                "auteur:metaAuthor\n" +
                "date:metaDate\n" +
                "---\n" +
                "This is *Sparta*";
        writer.write(inputContent);
        writer.close();

        File buildDirectory = new File(path + "/build");

        Thread thread = new Thread(() -> {
            assertEquals(0, new CommandLine(new Main()).execute("build", directoryTest.getPath(), "-w"));
        });
        thread.start();

        writer = new FileWriter(input);
        String inputContent2 = "titre:metaTitle\n" +
                "auteur:metaAuthor\n" +
                "date:metaDate\n" +
                "---\n" +
                "This is *Sparta*\n" +
                "This is a new line";
        writer.write(inputContent2);
        writer.close();
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(buildDirectory + "/input.html"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }
        assertTrue(output.contains("<p>This is <em>Sparta</em>"));
        assertTrue(output.contains("This is a new line</p>"));
        for(Thread t : Thread.getAllStackTraces().keySet()){
            if(t.getName().equals("FileWatcher")){
                t.interrupt();
            }
        }

        thread.interrupt();
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void buildShouldThrowWhenNoConfigIsPresent() {
        String path = directoryPath + "/noConfigTest";
        File directoryTest = new File(path);
        directoryTest.mkdirs();
        // Suppress warning
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));
        assertEquals(2, new CommandLine(new Main()).execute("build", path));
        System.setErr(original);
    }

    @AfterAll
    public static void cleanAll() throws IOException {
        System.setErr(original);
        Path path = Paths.get(root).normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());
    }
}
