package ch.heig_vd.app.utils;

import ch.heig_vd.app.Main;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class JsonParserTest {
    static String folderPath = "./test_folder_JsonParser";
    static String filePath = "./test_folder_JsonParser/config.json";
    static String fileFailPath = "./test_folder_JsonParser/config_fail.json";
    @BeforeClass
    public static void setup() throws IOException {
        File dir = new File(folderPath);
        dir.mkdir();
        File config = new File(filePath);
        if(config.createNewFile()){
            BufferedWriter writer = new BufferedWriter(new FileWriter(config));
            writer.write("{\n" +
                    "\"title\":\"a\",\n" +
                    "\"b\":\"c\"\n," +
                    "\"d\":1\n}");
            writer.flush();
            writer.close();
        }else{
            throw new RuntimeException("Could not write file");
        }

        File config2 = new File(fileFailPath);
        if(config2.createNewFile()){
            BufferedWriter writer = new BufferedWriter(new FileWriter(config2));
            writer.write("{\n" +
                    "\"title\":\"a\",\n" +
                    "\"b\":\"c\"\n," +
                    "\"d\":1\n");
            writer.flush();
            writer.close();
        }else{
            throw new RuntimeException("Could not write file");
        }
    }

    @Test
    public void jsonParserShouldReturnDataContainedInTheFile() throws IOException {
        String path1 = "./test_folder/mon/site/";
        ArrayList<Metadata> expected = new ArrayList<>();
        expected.add(new Metadata("title", "a"));
        expected.add(new Metadata("b", "c"));
        expected.add(new Metadata("d", "1"));

        final ArrayList<Metadata> data = new ArrayList<>();

        JsonParser parser = new JsonParser(new File(filePath), new JsonParserVisitor() {
            @Override
            public void visit(String field, String value) {
                data.add(new Metadata(field, value));
            }
        });
        assertEquals(expected.size(), data.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), data.get(i));
        }
    }
    @Test
    public void parserShouldThrowWhenFileIsNonFormatted() throws IOException {
        final ArrayList<Metadata> data = new ArrayList<>();

        assertThrows(JsonParseException.class, () -> {
            JsonParser parser = new JsonParser(new File(fileFailPath), new JsonParserVisitor() {
            @Override
            public void visit(String field, String value) {
                data.add(new Metadata(field, value));
            }
        });
        });
    }

    @Test
    public void parserShouldThrowWhenFileIsADirectory() throws IOException {
        final ArrayList<Metadata> data = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            JsonParser parser = new JsonParser(new File(folderPath), new JsonParserVisitor() {
                @Override
                public void visit(String field, String value) {
                    data.add(new Metadata(field, value));
                }
            });
        });
    }

    @AfterClass
    public static void cleanAll() throws IOException {
        Path path = Paths.get(folderPath).normalize().toAbsolutePath();
        System.out.println("Cleaning ALL");
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());

    }
}
