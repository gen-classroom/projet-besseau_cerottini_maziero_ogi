package ch.heig_vd.app.converter;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Tests for the converter
 */
public class ConverterTest {

    private static File jsonConfig = null;
    private static File input = null;
    private static final File templatePath = new File("./ConverterTest/template");

    @BeforeClass
    public static void setup() throws IOException {
        File testTree = new File("./ConverterTest/template");
        testTree.mkdirs();

        // Creates a test json config file
        jsonConfig = new File("./ConverterTest/conf.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(jsonConfig));
        writer.write("{\n" +
                "\"title\":\"globalTitle\",\n" +
                "\"b\":\"c\"\n" +
                "}");
        writer.flush();
        writer.close();

        // Creates test md file
        input = new File("./ConverterTest/input.md");
        writer = new BufferedWriter(new FileWriter(input));
        String inputContent = "title:localTitle\n" +
                "template:mytemplate\n" +
                "author:authorName\n" +
                "---\n" +
                "# Mon titre\n" +
                "## Mon sous-titre\n" +
                "Le contenu de mon article.\n" +
                "![Une image](./image.png)";
        writer.write(inputContent);
        writer.flush();
        writer.close();

        // Creates test template file
        File template = new File("./ConverterTest/template/mytemplate.html");
        writer = new BufferedWriter(new FileWriter(template));
        String templateContent = "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site:title}} | {{page:title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{page:author}}\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>";
        writer.write(templateContent);
        writer.flush();
        writer.close();
    }

    @Test
    public void mdFileShouldBeConvertedToHTMLFile() {
        try {
            // Converts
            Converter conv = new Converter(jsonConfig, templatePath);
            conv.markdownToHTML(input, "./ConverterTest/");

            // Checks output
            File outputFile = new File("./ConverterTest/input.html");
            BufferedReader reader = new BufferedReader(new FileReader(outputFile));

            // Tests
            StringBuilder output = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                output.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();

            // Assert
            String expectedOutput = "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<title>globalTitle | localTitle</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "authorName\n" +
                    "<h1>Mon titre</h1>\n" +
                    "<h2>Mon sous-titre</h2>\n" +
                    "<p>Le contenu de mon article.\n"+
                    "<img src=\"./image.png\" alt=\"Une image\" /></p>\n\n" +
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
        Converter conv = new Converter(jsonConfig, templatePath);
        conv.markdownToHTML(new File("./ConverterTest"), "./ConverterTest");
    }

    @Test(expected = RuntimeException.class)
    public void givenFileShouldHaveMarkdownExtension() {
        File file = new File("./ConverterTest/testFile.txt");
        Converter conv = new Converter(jsonConfig, templatePath);
        conv.markdownToHTML(file, "./");
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        Path path = Paths.get("./ConverterTest").normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());
    }
}
