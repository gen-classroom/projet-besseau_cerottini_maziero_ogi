package ch.heig_vd.app.utils;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TemplateInterpreterTest {

    private static ArrayList<Metadata> globalMeta = new ArrayList<>();


    private static ArrayList<Metadata> localMeta  = new ArrayList<>();
       @BeforeClass
    public static void setup() throws IOException {
        globalMeta.add(new Metadata("title", "MyWebsite"));
        globalMeta.add(new Metadata("description", "Website description"));
        localMeta.add(new Metadata("template", "template"));
        localMeta.add(new Metadata("description", "Website description"));

        File dir = new File("./templateInterpreter");
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

    @AfterClass
    public static void cleanUp() throws IOException {
        Path path = Paths.get("./templateInterpreter").normalize().toAbsolutePath();
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path.toFile());
    }
}
