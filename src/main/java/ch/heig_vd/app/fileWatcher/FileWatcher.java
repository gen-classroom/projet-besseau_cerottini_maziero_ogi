package ch.heig_vd.app.fileWatcher;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileWatcher {
    private final WatchService watchService;
    private final Thread thread;

    /**
     * Creates a file watcher to watch for change on a directory and subdirectory (except subDirectoryBuild)
     *
     * @param path The path to watch
     * @throws IOException
     */
    public FileWatcher(Path path, FileWatcherVisitor v) throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        walkAndRegisterDirectories(path);
        thread = new Thread(() -> {
            try {
                eventLoop(v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread.setName("FileWatcher");
        thread.start();
    }


    /**
     * Disable the fileWatcher
     */
    public void stop() {
        try {
            watchService.close();
        }catch (IOException e){
            System.err.println("Error closing the fileWatcher");
        }
    }



    /**
     * Event loop for the fileWatcher
     *
     * @param v The file visitor.
     */
    private void eventLoop(FileWatcherVisitor v) throws InterruptedException {
        WatchKey key;
        try {
            while ((key = watchService.take()) != null) {
                watchService.poll();
                Path dir = (Path) key.watchable();
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
                            System.err.println("Could not register file " + child + ". " + x.getMessage());
                        }
                    }
                }
                key.reset();
            }
        }catch (ClosedWatchServiceException e){
            System.out.println("File watcher stopped");
        }catch (InterruptedException e){
            stop();
            System.out.println("File watcher interrupted and closed gracefully");
        }

    }

    /**
     * Enable Watch on a directory
     *
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
     *
     * @param start The starting path
     * @throws IOException if an I/O error is thrown while registering the files
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        registerDirectory(start);
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
