package ch.heig_vd.app.utils;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.internal.text.Builder;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemplateInterpreter {
    private final Handlebars handlebars;

    TemplateInterpreter(File templateDirectory) throws IOException {
        if (!templateDirectory.exists()){
            throw new NoSuchFileException("Directory does not exist");
        }
        if (!templateDirectory.isDirectory()){
            throw new NotDirectoryException("template must be a directory");
        }
        FileTemplateLoader loader = new FileTemplateLoader(templateDirectory);
        loader.setSuffix(".html");
        loader.setPrefix(templateDirectory.getPath());
        handlebars = new Handlebars(loader);
        handlebars.registerHelper("md", new MarkdownHelper());
        handlebars.setPrettyPrint(true);
        // to throw when a value is missing
        handlebars.registerHelperMissing((context, options) -> {
            throw new HandlebarsException("Missing helper: "
                    + options.helperName + ". Helper Path: " + options.fn, null);
        });
    }

    public String generate(ArrayList<Metadata> global, ArrayList<Metadata> page, String content) throws IOException {
        Template template = null;
        Map<String, Object> hash = new HashMap<>();
        for (Metadata metadata: page) {
            if (metadata.getName().equals("template")){
                template = handlebars.compile(metadata.getContent());
            }else{
                hash.put("page:"+metadata.getName(), metadata.getContent());
            }
        }
        for (Metadata metadata: global) {
            hash.put("site:"+metadata.getName(), metadata.getContent());
        }
        hash.put("content", content);
        if (template == null){
            template = handlebars.compile("default");
        }

        Context context = Context.newBuilder(hash).resolver(MapValueResolver.INSTANCE).build();
        String result = template.apply(context);
        return result;
    }
}
