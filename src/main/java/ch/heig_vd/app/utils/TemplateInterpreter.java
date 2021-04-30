package ch.heig_vd.app.utils;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import java.io.File;
import java.util.ArrayList;

public class TemplateInterpreter {
    private final Handlebars handlebars;

    TemplateInterpreter(File templateDirectory){
        FileTemplateLoader loader = new FileTemplateLoader(templateDirectory);
        loader.setSuffix(".html");
        handlebars = new Handlebars();
        handlebars.registerHelper("md", new MarkdownHelper());
        handlebars.setPrettyPrint(true);
    }

    public String generate(ArrayList<Metadata> global, ArrayList<Metadata> page, String content){
        String layout =
    }
}
