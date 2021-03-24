package ch.heig_vd.app.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import java.io.*;

public class PageParserTest {
    @Test
    public void metadataShouldBeParsedRetrieved() {
        try {
            // Creates test file
            File input = new File("./input.md");
            String inputContent = "titre:metaTitle\n" +
                                  "auteur:metaAuthor\n" +
                                  "date:metaDate\n" +
                                  "---\n" +
                                  "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            Metadata meta = PageParser.exctractMetadata(input);

            // Checks validity
            assertEquals("metaTitle", meta.title);
            assertEquals("metaAuthor", meta.author);
            assertEquals("metaDate", meta.date);

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
            File input = new File("./input.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "NULL:metaDate\n" +
                    "---\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            Metadata meta = PageParser.exctractMetadata(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void exceptionWhenParsingMetadataInFileWithNoEndLine() {
        try {
            // Creates test file
            File input = new File("./input.md");
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "# MarkdownTitle";
            FileWriter writer = new FileWriter(input);
            writer.write(inputContent);
            writer.close();

            // Extracts the metadata
            Metadata meta = PageParser.exctractMetadata(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contentShouldBeEmptyIfNoEndOfMetadataIsProvided() {
        try {
            // Creates test file
            File input = new File("./input.md");
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
            File input = new File("./input.md");
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
        File dir = new File("./");
        PageParser.exctractMetadata(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMetaExtraction() {
        File file = new File("./testFile.txt");
        PageParser.exctractMetadata(file);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectoryForMarkdownExtraction() {
        File dir = new File("./");
        PageParser.extractMarkdownContent(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMarkdownExtraction() {
        File file = new File("./testFile.txt");
        PageParser.extractMarkdownContent(file);
    }

    @AfterAll
    public static void CleanUp() {
        File file = new File("./input.md");
        file.delete();
    }
}
