package ch.heig_vd.app.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class ConfigGenerator {

    public static String generateJsonMeta() throws IOException {

        // Creating the structure of the json file
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("title", "Title");
        rootNode.put("description", "A poisonous atmosphere");
        rootNode.put("domain", "poisonous.atmosphere.org");

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
