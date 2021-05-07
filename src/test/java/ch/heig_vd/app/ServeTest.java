package ch.heig_vd.app;


import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServeTest {

    static String root = "./test_folder";
    static String directoryPath = root + "/mon/site/build";
    static Thread server;

    @BeforeClass
    public static void setup() {
        File directoryTest = new File(directoryPath);
        directoryTest.mkdirs();

        server = new Thread(() -> new CommandLine(new Main()).execute( "serve", "test_folder/mon/site"));
        server.start();

        // Waits for server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void createIndex(String path) throws IOException {

        File htmlFile = new File(path + "/index.html");
        BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
        htmlWriter.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>My First Heading</h1>\n" +
                "\n" +
                "<p>My first paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        htmlWriter.flush();
        htmlWriter.close();
    }

    @Test
    @Order(0)
    public void testDownload() throws IOException{
        createIndex(directoryPath);
        URL url = new URL("http://localhost:8080/index.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        in.lines().forEach(System.out::println);
        in.close();
    }

    @Test(expected = FileNotFoundException.class)
    @Order(1)
    public void testFileNotFound() throws IOException {
        URL url = new URL("http://localhost:8080/not_found");
        url.openStream();
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
