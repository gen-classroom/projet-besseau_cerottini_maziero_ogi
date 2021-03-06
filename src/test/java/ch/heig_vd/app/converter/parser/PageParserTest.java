package ch.heig_vd.app.converter.parser;

import static org.junit.Assert.*;

import ch.heig_vd.app.converter.interpreter.TemplateInterpreter;
import ch.heig_vd.app.converter.utils.Metadata;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PageParserTest {
    @BeforeClass
    public static void setup() {
        File dir = new File("./PageParserTest");
        dir.mkdir();
    }

    @Test
    public void extractAllShouldThrowsInFileWithNoEndLineForMetaData() throws IOException {
        // Creates test file
        File input = new File("./PageParserTest/input3.md");
        String inputContent = "titre:metaTitle\n" +
                "auteur:metaAuthor\n" +
                "date:metaDate\n" +
                "# MarkdownTitle";
        FileWriter writer = new FileWriter(input);
        writer.write(inputContent);
        writer.close();
        ArrayList<Metadata> meta = new ArrayList<>();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            String content = PageParser.extractAll(input, meta);
        });
        assertEquals(exception.getMessage(), "Incorrect # MarkdownTitle metadata line\nCheck end of metadata separator \"---\"");
    }

    @Test
    public void extractAllShouldThrowsInFileWithWrongMetadataFields() throws IOException {
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

            ArrayList<Metadata> meta = new ArrayList<>();
            Exception exception = assertThrows(RuntimeException.class, () -> {
                String content = PageParser.extractAll(input, meta);
            });
            assertEquals(exception.getMessage(), "Incorrect wrongmetadatafilednoseparator metadata line\nCheck end of metadata separator \"---\"");

    }

    @Test
    public void extractAllShouldWork() {
        try {
            // Creates test file
            File input = new File("./PageParserTest/input5.md");
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

    @Test
    public void givenFileShouldNotBeDirectoryForExtraction() {
        File dir = new File("./PageParserTest");
        ArrayList<Metadata> meta = new ArrayList<>();
        assertThrows(RuntimeException.class, () -> PageParser.extractAll(dir, meta));
    }

    @Test
    public void givenFileShouldHaveMarkdownExtensionForExtraction() {
        File file = new File("./PageParserTest/testFile.txt");
        ArrayList<Metadata> meta = new ArrayList<>();
        assertThrows(RuntimeException.class, () -> PageParser.extractAll(file, meta));
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
