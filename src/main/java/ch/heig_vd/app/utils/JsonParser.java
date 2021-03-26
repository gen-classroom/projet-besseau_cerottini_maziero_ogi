package ch.heig_vd.app.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class JsonParser {
    JsonParser(File f, JsonParserVisitor visitor) throws IOException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(f);

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
            visitor.visit(field.getKey().toString(), field.getValue().toString());

        }
    }

    public static void main(String[] args) throws IOException {
        final ArrayList<Metadata> data = new ArrayList<>();

        JsonParser c = new JsonParser(new File("/home/leonard/IdeaProjects/BA4/GEN/projet-besseau_cerottini_maziero_ogi/test.json"), new JsonParserVisitor() {
            @Override
            public void visit(String field, String value) {
                data.add(new Metadata(field, value));
            }
        });
        for (Metadata m : data){
            System.out.println(m);
        }

    }
}
