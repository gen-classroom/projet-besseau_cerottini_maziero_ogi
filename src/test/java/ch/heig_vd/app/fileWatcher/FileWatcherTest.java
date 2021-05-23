package ch.heig_vd.app.fileWatcher;

import java.io.IOException;
import java.nio.file.Path;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        FileWatcher watcher = new FileWatcher(Path.of("./test"), (name, path) -> System.out.format("%s: %s\n", name, path));
    }
}
