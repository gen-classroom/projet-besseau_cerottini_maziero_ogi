package ch.heig_vd.app.utils;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
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
    private static ArrayList<Metadata> localMeta = new ArrayList<>();

    @BeforeClass
    public static void setup() throws IOException {
        globalMeta.add(new Metadata("title", "MyWebsite"));
        globalMeta.add(new Metadata("description", "Website description"));
        localMeta.add(new Metadata("template", "templatefile"));
        localMeta.add(new Metadata("author", "Website author"));

        File dir = new File("./templateInterpreter");
        dir.mkdir();
    }

    @Test
    public void templateDirectoryMissingShouldThrows() throws IOException {
        String path = "./templateInterpreter/missing/";
        File dir = new File(path);
        Exception exception = assertThrows(NoSuchFileException.class, () -> {
            TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        });

    }

    @Test
    public void templateDirectoryNotADirectoryShouldThrows() throws IOException {
        String path = "./templateInterpreter/notAFile/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "template.html");
        file.createNewFile();
        Exception exception = assertThrows(NotDirectoryException.class, () -> {
            TemplateInterpreter templateInterpreter = new TemplateInterpreter(file);
        });
    }


    // test if directory is empty and wrong template is asked
    @Test
    public void templateNotFoundShouldThrows() throws IOException {
        String path = "./templateInterpreter/notFound/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "wrongtemplate.html");
        file.createNewFile();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        ArrayList<Metadata> localMeta = new ArrayList<>();
        localMeta.add(new Metadata("author", "Website author"));
        localMeta.add(new Metadata("template", "templateA"));
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            templateInterpreter.generate(globalMeta, localMeta, mdContent);
        });
    }

    @Test
    public void noMatchingMetaRequiredInTemplateShouldThrows() throws IOException {
        String path = "./templateInterpreter/noMatch/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "templateA.html");
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site.title}} | {{page.title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer.flush();
        writer.close();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        ArrayList<Metadata> localMeta = new ArrayList<>();
        localMeta.add(new Metadata("author", "Website author"));
        localMeta.add(new Metadata("template", "templateA"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            templateInterpreter.generate(globalMeta, localMeta, mdContent);
        });
    }

    // Maybe check if a warning is displayed if any
    @Test
    public void tooManyMetaInFileShouldWork() throws IOException {
        String path = "./templateInterpreter/tooMany/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "templateA.html");
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site_title}} | {{page_title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer.flush();
        writer.close();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        ArrayList<Metadata> localMeta = new ArrayList<>();
        localMeta.add(new Metadata("author", "Website author"));
        localMeta.add(new Metadata("template", "templateA"));
        localMeta.add(new Metadata("title", "test"));
        templateInterpreter.generate(globalMeta, localMeta, mdContent);
    }

    // Should use default template file
    @Test
    public void shouldWorkWithNoTemplateInFileMetadata() throws IOException {
        String path = "./templateInterpreter/work_default/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "default.html");
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site_title}} | {{page_title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer.flush();
        writer.close();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        ArrayList<Metadata> localMeta = new ArrayList<>();
        localMeta.add(new Metadata("author", "Website author"));
        localMeta.add(new Metadata("title", "test"));
        templateInterpreter.generate(globalMeta, localMeta, mdContent);
        // TODO check if correspond to template with data
    }

    // Should use given template
    @Test
    public void shouldWorkWithTemplateGivenInFileMetadata() throws IOException {
        String path = "./templateInterpreter/work_template/";
        File dir = new File(path);
        dir.mkdir();
        File file = new File(path + "default.html");
        File file2 = new File(path + "templateA.html");
        file.createNewFile();
        file2.createNewFile();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site_title}} | {{page_title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer.flush();
        writer.close();
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file2)));
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>{{site_title }} | {{page_title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "{{page_author}}\n" +
                "{{md content}}\n" +
                "</body>\n" +
                "</html>");
        writer.flush();
        writer.close();
        TemplateInterpreter templateInterpreter = new TemplateInterpreter(dir);
        ArrayList<Metadata> localMeta = new ArrayList<>();
        localMeta.add(new Metadata("author", "Website author"));
        localMeta.add(new Metadata("template", "templateA"));
        localMeta.add(new Metadata("title", "test"));
        templateInterpreter.generate(globalMeta, localMeta, mdContent);
        // TODO check that content is used with template A and not default
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
