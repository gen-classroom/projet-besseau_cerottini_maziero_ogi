package ch.heig_vd.app.utils;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PageParserTest {
    @BeforeClass
    public static void setup() throws IOException {
        File dir = new File("./PageParserTest");
        dir.mkdir();
    }

    @Test
    public void metadataShouldBeParsedRetrieved() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input1.md");
            String inputContent = "titre:metaTitle\n" +
                                  "myMetaName:metaContentText\n" +
                                  "date:metaDate\n" +
                                  "---\n" +
                                  "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);

            // Checks validity
            assertEquals("titre", meta.get(0).getName());
            assertEquals("myMetaName", meta.get(1).getName());
            assertEquals("date", meta.get(2).getName());
            assertEquals("metaTitle", meta.get(0).getContent());
            assertEquals("metaContentText", meta.get(1).getContent());
            assertEquals("metaDate", meta.get(2).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void exceptionWhenParsingWrongMetadataFields() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input2.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "wrongmetadatafilednoseparator\n" +
                    "---\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void exceptionWhenParsingMetadataInFileWithNoEndLine() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input3.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void markdownContentShouldBeParsedAndExtracted() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input4.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "mymeta:metaContent\n" +
                    "---\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the markdown content
            String content = PageParser.extractMarkdownContent(input);

            assertEquals("# MarkdownTitle" + System.lineSeparator(), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void markdownAndMetaShouldBeParsedAndExtracted() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input4.md");
            String inputContent = "titre:metaTitle\n" +
                    "myMetaName:metaContentText\n" +
                    "date:metaDate\n"+
                    "---\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();


            ArrayList<Metadata> meta = new ArrayList<>();
            String content = PageParser.extractAll(input, meta);

            assertEquals("titre", meta.get(0).getName());
            assertEquals("myMetaName", meta.get(1).getName());
            assertEquals("date", meta.get(2).getName());
            assertEquals("metaTitle", meta.get(0).getContent());
            assertEquals("metaContentText", meta.get(1).getContent());
            assertEquals("metaDate", meta.get(2).getContent());
            assertEquals("# MarkdownTitle" + System.lineSeparator(), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectoryForMetaExtraction() {
        File dir = new File("./PageParserTest");
        PageParser.extractMetadata(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMetaExtraction() {
        File file = new File("./PageParserTest/testFile.txt");
        PageParser.extractMetadata(file);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectoryForMarkdownExtraction() {
        File dir = new File("./PageParserTest");
        PageParser.extractMarkdownContent(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMarkdownExtraction() {
        File file = new File("./PageParserTest/testFile.txt");
        PageParser.extractMarkdownContent(file);
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        Path path = Paths.get("./PageParserTest").normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());
    }
}
