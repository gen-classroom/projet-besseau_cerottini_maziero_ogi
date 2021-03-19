package ch.heig_vd.app;

import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class CleanTest {


    @Test
    public void cleanShouldDeleteBuildFolder() throws IOException {
        File dir = new File("./mon/site/build");
        dir.mkdirs();
        new CommandLine(new Main()).execute(new String[]{"statique", "Clean"});

        assertFalse(dir.exists());
    }
}
