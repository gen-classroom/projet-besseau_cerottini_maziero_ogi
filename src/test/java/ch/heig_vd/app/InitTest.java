package ch.heig_vd.app;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class InitTest {


    @Test
    public void initShouldCreateDirectories() throws IOException {
        File dir = new File("./mon/site/");
        assertFalse(dir.exists());
    }

    @Test
    public void initShouldCreateJson() throws IOException {

    }

    @Test
    public void initShouldCreateMd() throws IOException {

    }
}
