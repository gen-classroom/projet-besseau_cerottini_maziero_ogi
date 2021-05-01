package ch.heig_vd.app.generator;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TemplateGenerator {

    public static String generateIndex() throws IOException {
        return "title:localTitle\n" +
                "template:mytemplate\n" +
                "author:authorName\n" +
                "---\n" +
                "#My too classy site !\n"+
                "Author : a huge bg\n"+
                "Date:" + new SimpleDateFormat(" dd.MM.yyyy ").format(new Date());
    }

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
