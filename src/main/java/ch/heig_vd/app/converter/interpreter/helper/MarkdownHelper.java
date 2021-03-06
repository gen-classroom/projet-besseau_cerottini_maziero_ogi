package ch.heig_vd.app.converter.interpreter.helper;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 *
 * Custom Markdown Helper to transform markdown into html with handlebars
 *
 * @author Besseau Léonard
 * @author Marco Maziero
 */
public class MarkdownHelper implements Helper<Object> {

    @Override
    public Object apply(final Object context, final Options options) {
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
