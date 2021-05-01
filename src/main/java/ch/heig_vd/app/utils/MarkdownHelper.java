package ch.heig_vd.app.utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;

public class MarkdownHelper implements Helper<Object> {
    public static final Helper<Object> INSTANCE = new MarkdownHelper();

    @Override
    public Object apply(final Object context, final Options options)
            throws IOException {
        if (options.isFalsy(context)) {
            return "";
        }
        String markdown = context.toString();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().attributeProviderFactory(contextProvider -> new LinkAttributeProvider()).build();
        return new Handlebars.SafeString(renderer.render(document));
    }

}
