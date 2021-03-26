package ch.heig_vd.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigParser {
    ConfigData data;

    ConfigParser(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        data = mapper.readValue(new File(path), ConfigData.class);
    }
}
