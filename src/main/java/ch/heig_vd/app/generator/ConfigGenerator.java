package ch.heig_vd.app.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

/**
 * Class used to generate a basic JSON configuration file
 *
 * @author Cerottini Alexandra
 * @author Ogi Nicolas
 */
public class ConfigGenerator {

    /**
     * Method used to generate the JSON file
     * @return Structure of the JSON object in String
     * @throws JsonProcessingException if an error occurred while processing the JSON
     */
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
