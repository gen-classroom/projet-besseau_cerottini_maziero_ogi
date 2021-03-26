package ch.heig_vd.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ConfigParser {
    private final ConfigData data;

    ConfigParser(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        data = mapper.readValue(f, ConfigData.class);
    }

    public ArrayList<Metadata> getData() throws IllegalAccessException {
        ArrayList<Metadata> out = new ArrayList<>();
        for (Field field : ConfigData.class.getFields()){
            out.add(new Metadata(field.getName(), field.get(data).toString()));
        }

        return out;
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        ConfigParser c = new ConfigParser(new File("/home/leonard/IdeaProjects/BA4/GEN/projet-besseau_cerottini_maziero_ogi/test.json"));
        for (Metadata m : c.getData()){
            System.out.println(m);
        }
    }
}
