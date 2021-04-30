package ch.heig_vd.app.utils;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TemplateInterpreterTest {

    private static ArrayList<Metadata> globalMeta = new ArrayList<>();
    private static String mdContent = "# Mon titre\n" +
                                      "## Mon sous-titre\n" +
                                      "Le contenu de mon article.\n" +
                                      "![Une image](./image.png)";
    private static ArrayList<Metadata> localMeta  = new ArrayList<>();
       @BeforeClass
    public static void setup() throws IOException {
        globalMeta.add(new Metadata("title", "MyWebsite"));
        globalMeta.add(new Metadata("description", "Website description"));
        localMeta.add(new Metadata("template", "templatefile"));
        localMeta.add(new Metadata("author", "Website author"));

        File dir = new File("./templateInterpreter");
        dir.mkdir();
    }

    @Test(expected = NoSuchFileException.class)
    public void templateDirectoryMissingShouldThrows() throws IOException {
        String path = "./templateInterpreter/missing";
        File dir = new File(path);
        Exception exception = assertThrows(NoSuchFileException.class, () -> {
            TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        });

    }

    @Test(expected = NotDirectoryException.class)
    public void templateDirectoryNotADirectoryShouldThrows() throws IOException {
        String path = "./templateInterpreter/notAFile";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path+"template");
        file.createNewFile();
        Exception exception = assertThrows(NotDirectoryException.class, () -> {
            TemplateInterpreter templateInterpreter = new TemplateInterpreter(file);
        });
    }


    // test if directory is empty and wrong template is asked
    @Test(expected = RuntimeException.class)
    public void templateNotFoundShouldThrows() throws IOException {
        String path = "./templateInterpreter/notAFile";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path+"wrongtemplate");
        file.createNewFile();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        templateInterpreter.generate(globalMeta, localMeta, mdContent);
    }

    @Test(expected = RuntimeException.class)
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
