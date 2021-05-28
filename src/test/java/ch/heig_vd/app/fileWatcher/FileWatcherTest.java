package ch.heig_vd.app.fileWatcher;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileWatcherTest {
    private static final String path = "./testFileWatcher1";

    @BeforeAll
    public static void setup() {
        File directoryTest = new File(path);
        directoryTest.mkdirs();
    }

    @Test
    public void fileWatcherShouldDetectChangesInFolder() throws IOException {
        ArrayList<String> output = new ArrayList<>();
        FileWatcher watcher = new FileWatcher(Path.of(path), (name, path1) -> {
            // print out event
            output.add(name+" "+path1);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new File(path + "/a").createNewFile();
        File b = new File(path + "/b");
        b.createNewFile();
        b.delete();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watcher.stop();
        assertTrue(output.contains("ENTRY_CREATE ." + File.separator + "testFileWatcher1" + File.separator + "a"));
        assertTrue(output.contains("ENTRY_CREATE ." + File.separator + "testFileWatcher1" + File.separator + "b"));
        assertTrue(output.contains("ENTRY_DELETE ." + File.separator + "testFileWatcher1" + File.separator + "b"));
    }

    @AfterAll
    public static void cleanAll() throws IOException {
        Path path1 = Paths.get(path).normalize().toAbsolutePath();
        if (!path1.toFile().exists()) {
            throw new IllegalArgumentException("Directory does not exists");
        }
        FileUtils.deleteDirectory(path1.toFile());

    }
}
