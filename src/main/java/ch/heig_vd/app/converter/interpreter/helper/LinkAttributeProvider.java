package ch.heig_vd.app.converter.interpreter.helper;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

/**
 * Custom transformer to transform link to other markdown document to html link+
 *
 * @author Besseau Léonard
 */
public class LinkAttributeProvider implements AttributeProvider {
    @Override
    public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
        if (node instanceof Link) {
            String href = attributes.get("href");
            if (href.endsWith(".md")) {
                int dotPos = href.lastIndexOf(".");
                String strFilename = href.substring(0, dotPos);
                attributes.put("href", strFilename + ".html");
            }
        }
    }
}
