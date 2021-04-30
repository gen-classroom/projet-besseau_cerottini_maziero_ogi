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
        handlebars = new Handlebars();
        handlebars.registerHelper("md", new MarkdownHelper());
        handlebars.setPrettyPrint(true);
    }

    public String generate(ArrayList<Metadata> global, ArrayList<Metadata> page, String content) throws IOException {
        Template a = null;
        Map<String, Object> hash = new HashMap<>();
        for (Metadata metadata: page) {
            if (metadata.getName().equals("template")){
                a = handlebars.compile(metadata.getContent());
            }else{
                hash.put(metadata.getName(), metadata.getContent());
            }
        }
        if (a == null){
            a = handlebars.compile("default");
        }



        Context context = Context.newBuilder(hash).resolver(MapValueResolver.INSTANCE).build();

        return "TODO";
    }
}
