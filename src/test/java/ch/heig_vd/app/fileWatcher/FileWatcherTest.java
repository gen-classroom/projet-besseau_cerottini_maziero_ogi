package ch.heig_vd.app.fileWatcher;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class FileWatcherTest {

//    @Test
//    public void fileWatcherShouldDetectChangesInFolder() throws IOException, InterruptedException {
//        String path = "./testFileWatcher1";
//        File dir = new File(path);
//        dir.mkdir();
//        FileWatcher watcher = new FileWatcher(Path.of(path));
//        new File(path + "/a").createNewFile();
//        File b = new File(path + "/b");
//        b.createNewFile();
//        StringBuilder stringBuilder = new StringBuilder();
//        watcher.events(new FileWatcherVisitor() {
//            @Override
//            public void visit(String name, Path path) {
//                // print out event
//                stringBuilder.append(name);
//                stringBuilder.append(" ");
//                stringBuilder.append(path);
//                System.out.format("%s: %s\n", name, path);
//            }
//        });
//        assertEquals(stringBuilder.toString(), "");
//    }
}
