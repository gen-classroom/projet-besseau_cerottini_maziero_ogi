package ch.heig_vd.app.utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TemplateInterpreterTest {

    private static ArrayList<Metadata> globalMeta = new ArrayList<>();
    private static ArrayList<Metadata>  = new ArrayList<>();

    @BeforeClass
    public static void setup() throws IOException {
        File dir = new File("./PageParser");
        dir.mkdir();
    }

    @Test
    public void templateDirectoryNotADirectoryShouldThrows() {

    }

    @Test
    public void emptyTemplateDirectoryShouldThrows() {

    }

    @Test
    public void templateNotFoundShouldThrows() {

    }


    @Test
    public void noMatchingMetaRequiredInTemplateShouldThrows() {

    }

    // Maybe check if a warning is displayed if any
    @Test
    public void tooManyMetaInFileShouldWork() {

    }

    // Should use default template file
    @Test
    public void shouldWorkWithNoTemplateInFileMetadata() {

    }

    // Should use given template
    @Test
    public void shouldWorkWithTemplateGivenInFileMetadata() {

    }
}
