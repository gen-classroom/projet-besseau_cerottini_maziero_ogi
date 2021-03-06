package ch.heig_vd.app.converter.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * This class allows to parse Json
 *
 * @author Besseau Leonard
 */
public class JsonParser {

    /**
     * Parse a JSON file and given the content to a visitor for further use
     *
     * @param f       The file to parse
     * @param visitor The visitor to use. (Get the key and the value of each entry)
     * @throws IOException If an IO exception occurs while parsing
     */
    public static void parse(File f, JsonParserVisitor visitor) throws IOException {
        if (f.isDirectory()) {
            throw new IllegalArgumentException("File must not be a directory");
        }
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(f);

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            visitor.visit(field.getKey(), field.getValue().asText());
        }
    }
}
