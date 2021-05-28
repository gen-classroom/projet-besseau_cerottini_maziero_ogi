package ch.heig_vd.app.fileWatcher;

import java.nio.file.Path;

public interface FileWatcherVisitor {

    /**
     * Visitor for the file Watcher
     * @param name The name of the file
     * @param path The path of the file
     */
    void visit(String name, Path path);
}
