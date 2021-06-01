package ch.heig_vd.app.converter.interpreter;

import ch.heig_vd.app.converter.utils.Metadata;
import ch.heig_vd.app.converter.interpreter.helper.MarkdownHelper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles template management and file conversion
 *
 * @author Besseau Léonard
 * @author Marco Maziero
 */
public class TemplateInterpreter {
    private final Handlebars handlebars;

    /**
     * Create a template interpreter. Can insert content into template with tag specified by {{}}
     * Expect template to be ending with .html.
     * Can transform markdown into html if specified by {{md XXXXX}}
     *
     * @param templateDirectory the directory where all the templates are stored
     * @throws IOException File does not exist or File is not a directory
     */
    public TemplateInterpreter(File templateDirectory) throws IOException {
        if (!templateDirectory.exists()) {
            throw new NoSuchFileException("Directory does not exist");
        }
        if (!templateDirectory.isDirectory()) {
            throw new NotDirectoryException("template must be a directory");
        }
        FileTemplateLoader loader = new FileTemplateLoader(templateDirectory);
        loader.setSuffix(".html");
        handlebars = new Handlebars(loader);
        handlebars.registerHelper("md", new MarkdownHelper());
        handlebars.setPrettyPrint(true);

        //Set handler when a value is missing
        handlebars.registerHelperMissing((context, options) -> {
            throw new HandlebarsException("Missing parameter: "
                    + options.helperName + ". Parameter Path: " + options.fn, null);
        });
    }

    /**
     * Generates html valid content by inserting data into template
     *
     * @param global Site metadata
     * @param page page metadata
     * @param content the content of the page
     * @return an html valid page
     * @throws IOException Invalid template Access or missing element in data for template
     */
    public String generate(ArrayList<Metadata> global, ArrayList<Metadata> page, String content) throws IOException {
        if (global == null || page == null || content == null){
            throw new IllegalArgumentException("Input should not be null");
        }
        Template template = null;
        Map<String, Object> hash = new HashMap<>();
        for (Metadata metadata : page) {
            if (metadata.getName().equals("template")) {
                try {
                    template = handlebars.compile(metadata.getContent());
                }catch (FileNotFoundException e){
                    throw new FileNotFoundException("Template file "+metadata.getContent()+" not found.");
                }

            } else {
                hash.put("page:" + metadata.getName(), metadata.getContent());
            }
        }
        for (Metadata metadata : global) {
            hash.put("site:" + metadata.getName(), metadata.getContent());
        }
        hash.put("content", content);
        if (template == null) {
            template = handlebars.compile("default");
        }

        Context context = Context.newBuilder(hash).resolver(MapValueResolver.INSTANCE).build();
        String result = template.apply(context);
     //   context.destroy(); // not sure if useful
        return result;
    }
}
