package ch.heig_vd.app.utils;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
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
        File dir = new File("./PageParser");
        dir.mkdir();
    }

    @Test
    public void metadataShouldBeParsedRetrieved() {
        try {
            // Creates test file
            File input = new File("./PageParser/input.md");
            String inputContent = "titre:metaTitle\n" +
                                  "auteur:metaAuthor\n" +
                                  "date:metaDate\n" +
                                  "---\n" +
                                  "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);

            // Checks validity
            assertEquals("metaTitle", meta.get(0).getContent());
            assertEquals("metaAuthor", meta.get(1).getContent());
            assertEquals("metaDate", meta.get(2).getContent());

            // Deletes test file
            input.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void exceptionWhenParsingWrongMetadataFields() {
        try {
            // Creates test file
            File input = new File("./PageParser/input.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "NULL:metaDate\n" +
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
            File input = new File("./PageParser/input.md");
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
    public void contentShouldBeEmptyIfNoEndOfMetadataIsProvided() {
        try {
            // Creates test file
            File input = new File("./PageParser/input.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the markdown content
            String content = PageParser.extractMarkdownContent(input);
            input.delete();

            assertEquals("", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void markdownContentShouldBeParsedAndExtracted() {
        try {
            // Creates test file
            File input = new File("./PageParser/input.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "---\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the markdown content
            String content = PageParser.extractMarkdownContent(input);

            assertEquals("# MarkdownTitle", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectoryForMetaExtraction() {
        File dir = new File("./PageParser");
        PageParser.extractMetadata(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMetaExtraction() {
        File file = new File("./PageParser/testFile.txt");
        PageParser.extractMetadata(file);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectoryForMarkdownExtraction() {
        File dir = new File("./PageParser");
        PageParser.extractMarkdownContent(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMarkdownExtraction() {
        File file = new File("./PageParser/testFile.txt");
        PageParser.extractMarkdownContent(file);
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        Path path = Paths.get("./PageParser").normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());
    }
}
