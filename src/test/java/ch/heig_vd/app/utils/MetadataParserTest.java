package ch.heig_vd.app.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class MetadataParserTest {
    @Test
    public void metadataShouldBeParsedRetrievedAndDeletedFromFile() {
        try {
            // Creates test file
            File input = new File("./input.md");
            FileWriter writer = new FileWriter(input);
            writer.write("titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "---\n" +
                    "# MarkdownTitle");
            writer.close();

            // extracts the meta
            Metadata meta = MetadataParser.exctractMetadata(input);

            // Reads file to check if meta are gone
            BufferedReader reader = new BufferedReader(new FileReader(input));
            String content = reader.readLine();
            assertEquals("metaTitle", meta.title);
            reader.close();

            // Checks validity
            assertEquals("metaTitle", meta.title);
            assertEquals("metaAuthor", meta.author);
            assertEquals("metaDate", meta.date);
            assertEquals("# MarkdownTitle", content);

            // Deletes test file
            input.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectory() {
        File dir = new File("./");
        MetadataParser.exctractMetadata(dir);
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtension() {
        File file = new File("./testFile.txt");
        MetadataParser.exctractMetadata(file);
    }
}
