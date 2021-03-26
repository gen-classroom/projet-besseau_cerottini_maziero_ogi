package ch.heig_vd.app.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import java.io.*;
import java.util.ArrayList;

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
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);
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
            ArrayList<Metadata> meta = PageParser.extractMetadata(input);
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
        PageParser.extractMetadata(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtensionForMetaExtraction() {
        File file = new File("./testFile.txt");
        PageParser.extractMetadata(file);
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
    public static void cleanUp() {
        File file = new File("./input.md");
        file.delete();
    }
}
