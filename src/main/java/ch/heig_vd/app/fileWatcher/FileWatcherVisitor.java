package ch.heig_vd.app.fileWatcher;

import java.nio.file.Path;

public interface FileWatcherVisitor {

    void visit(String name, Path path);
}
