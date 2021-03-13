package ch.heig_vd.app;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class CleanTest {


    @Test
    public void cleanShouldDeleteBuildFolder() throws IOException {
        File dir = new File("./mon/site/build");
        dir.mkdirs();
        FileUtils.deleteDirectory(dir);

        assertFalse(dir.exists());
    }
}
