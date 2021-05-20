package ch.heig_vd.app.fileWatcher;

import java.io.IOException;
import java.nio.file.Path;

public class FileWatcherTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        FileWatcher watcher = new FileWatcher(Path.of("./test"));
        watcher.events();
    }
}
