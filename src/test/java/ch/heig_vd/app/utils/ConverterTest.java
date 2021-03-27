package ch.heig_vd.app.utils;

import org.apache.commons.io.FilenameUtils;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Tests for the converter
 */
public class ConverterTest {

    @Test
    public void mdFileShouldBeConvertedToHTMLFile() {
        try {
            // Creates a test json config file
            File jsonConfig = new File("./conf.json");
            BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonConfig));
            jsonWriter.write("{\n" +
                    "\"title\":\"a\",\n" +
                    "\"b\":\"c\"}");
            jsonWriter.flush();
            jsonWriter.close();

            // Creates test md file
            File input = new File("./input.md");
            FileWriter writer = new FileWriter(input);
            String inputContent = "titre:metaTitle\n" +
                    "auteur:metaAuthor\n" +
                    "date:metaDate\n" +
                    "---\n" +
                    "This is *Sparta*";
            writer.write(inputContent);
            writer.close();

            // Converts
            Converter conv = new Converter(jsonConfig);
            conv.markdownToHTML(input, "./");

            // Checks output
            File ouput = new File("./input.html");
            BufferedReader reader = new BufferedReader(new FileReader(ouput));

            // Tests
            StringBuilder output = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                output.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();

            // Deletes the files
            input.delete();
            ouput.delete();

            // Assert
            String expectedOutput = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>a</title>\n" +
                    "<meta name=\"b\" content=\"c\">\n" +
                    "<meta name=\"title\" content=\"metaTitle\">\n" +
                    "<meta name=\"author\" content=\"metaAuthor\">\n" +
                    "<meta name=\"date\" content=\"metaDate\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p>This is <em>Sparta</em></p>\n" +
                    "</body>\n" +
                    "</html>\n";

            assertEquals(expectedOutput, output.toString());

        } catch (IOException e) {
            System.out.println("An error occured when working with the files");
            fail();
        }
    }



    @Test(expected = RuntimeException.class)
    public void givenFileShouldNotBeDirectory() {
        File jsonConfig = new File("./conf.json");
        File dir = new File("./");
        Converter conv = new Converter(jsonConfig);
        conv.markdownToHTML(dir, "./");
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtension() {
        File jsonConfig = new File("./conf.json");
        File file = new File("./testFile.txt");
        Converter conv = new Converter(jsonConfig);
        conv.markdownToHTML(file, "./");
    }

    @Test
    public void mdFileWithNoValidMetadataShouldBeRejected() {
        try {
            // Creates a test json config file
            File jsonConfig = new File("./conf.json");
            BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonConfig));
            jsonWriter.write("{\n" +
                    "\"title\":\"a\",\n" +
                    "\"b\":\"c\"}");
            jsonWriter.flush();
            jsonWriter.close();

            // Creates test md file
            File input = new File("./input.md");
            FileWriter writer = new FileWriter(input);
            String inputContent = "titre:metaTitle\n" +
                    "date:metaDate\n" +
                    "---\n" +
                    "This is *Sparta*";
            writer.write(inputContent);
            writer.close();

            // Records console
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            PrintStream sysOut = System.err;
            System.setErr(ps);

            // Converts
            Converter conv = new Converter(jsonConfig);
            conv.markdownToHTML(input, "./");

            // Checks console
            String expected = "File input.md could not be parsed and was not added to destination\n" +
                    "Error : Metadata are incomplete" + System.lineSeparator();
            String result = os.toString();
            FilenameUtils.normalize(result);
            FilenameUtils.normalize(expected);
            System.err.flush();
            System.setErr(sysOut);
            assertEquals(expected, result);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void cleanUp() {
        File file = new File("testFile.txt");
        File jsonConfig = new File("./conf.json");
        File input = new File("./input.md");
        file.delete();
        jsonConfig.delete();
        input.delete();
    }
}
