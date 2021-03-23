package ch.heig_vd.app.utils;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Tests for the converter
 */
public class ConverterTest {

    @Test
    public void mdFileShouldBeConvertedToHTMLFile() {
        try {
            // Creates test file
            File input = new File("src/input.md");
            FileWriter writer = new FileWriter(input);
            writer.write("This is *Sparta*");
            writer.close();

            // Converts
            Converter.MarkdownToHTML(input, "src/");

            // Checks ouput
            File ouput = new File("src/input.html");
            Scanner reader = new Scanner(ouput);

            // Tests
            String line = reader.nextLine();
            reader.close();

            // Deletes the files
            input.delete();
            ouput.delete();

            // Assert
            assertEquals("<p>This is <em>Sparta</em></p>", line);

        } catch (IOException e) {
            System.out.println("An error occured when working with the files");
            fail();
        }
    }
}
