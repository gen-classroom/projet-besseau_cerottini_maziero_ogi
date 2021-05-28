package ch.heig_vd.app.generator;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Cerottini Alexandra
 * @author Ogi Nicolas
 */
public class TemplateGenerator {

    /**
     * Generate an index example
     *
     * @return a string containing the index example
     */
    public static String generateIndex() {
        return "title:localTitle\n" +
                "author:authorName\n" +
                "---\n" +
                "# My too classy site !\n" +
                "Author : a huge bg\n" +
                "Date:" + new SimpleDateFormat(" dd.MM.yyyy ").format(new Date());
    }

    /**
     * Generate a template
     *
     * @return a string containing a basic template
     */
    public static String generateTemplate() {

        return "<html lang=\"en\">\n" +
                "<head>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <title>{{site:title}} | {{page:title}}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "      {{md content}}\n" +
                "</body>\n" +
                "</html>";
    }
}
