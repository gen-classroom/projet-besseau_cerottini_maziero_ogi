package ch.heig_vd.app.generator;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TemplateGenerator {

    public static String generateIndex() throws IOException {
        return "# My too classy site !\nAuthor : a huge bg\nDate : " + new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static String generateTemplate() {

        return  "<html lang=\"en\">\n" +
                "<head>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <title>{{ GLOBAL.titre }} | {{ PAGE.titre }}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "      {{ content }}\n" +
                "</body>\n" +
                "</html>";
    }
}
