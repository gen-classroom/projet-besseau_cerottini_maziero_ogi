package ch.heig_vd.app.fileWatcher;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileWatcher {
    private final WatchService watchService;

    /**
     * Creates a file watcher to watch for change on a directory and subdirectory (except subDirectoryBuild)
     * @param path The path to watch
     * @throws IOException
     */
    public FileWatcher(Path path) throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        walkAndRegisterDirectories(path);
    }

    /**
     * Event loop for the fileWatcher
     *
     * @param v The file visitor.
     * @throws InterruptedException
     */
    public void events(FileWatcherVisitor v) throws InterruptedException {
        // wait for key to be signalled
        WatchKey key;
        while ((key = watchService.take()) != null) {

            Path dir = (Path)key.watchable();
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>) event).context();
                Path child = dir.resolve(name);

                v.visit(event.kind().name(), child);
                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                        }
                    } catch (IOException x) {
                        System.err.println("Could not register file "+child+". "+x.getMessage());
                    }
                }
            }
            key.reset();
        }
    }

    /**
     * Enable Watch on a directory
     * @param dir The path of the directory to watch
     * @throws IOException If an I/O error occurs
     */
    private void registerDirectory(Path dir) throws IOException {
        dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_CREATE);
    }

    /**
     * Recursively add files and directories
     * @param start The starting path
     * @throws IOException if an I/O error is thrown while registering the files
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!attrs.isDirectory() || !dir.endsWith("build")) {
                    registerDirectory(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
