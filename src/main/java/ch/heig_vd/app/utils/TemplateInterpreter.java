package ch.heig_vd.app.utils;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import java.io.File;

public class TemplateInterpreter {
    private final Handlebars handlebars;

    TemplateInterpreter(File templateFile){
        handlebars = new Handlebars(new CompositeTemplateLoader(new FileTemplateLoader(templateFile), new ClassPathTemplateLoader()));
        handlebars.registerHelper("md", new MarkdownHelper());
        handlebars.setPrettyPrint(true);
    }
}
