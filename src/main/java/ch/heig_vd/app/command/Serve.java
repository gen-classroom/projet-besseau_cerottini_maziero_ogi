package ch.heig_vd.app.command;

import ch.heig_vd.app.fileWatcher.FileWatcher;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

@CommandLine.Command(name = "serve",
        description = "Serve a static site")

public class Serve implements Runnable {
    @CommandLine.Parameters(description = "Path of site to serve.")
    private static String site;
    @CommandLine.Option(names = {"-w", "--watcher"}, paramLabel = "Watcher", description = "Enable file watcher to automate")
    boolean watcher;

    private static final int PORT = 8080;

    public void run() {

        // Serve the site
        Javalin.create(config ->
                config.addStaticFiles(Paths.get(site).resolve("build").toAbsolutePath().toString(), Location.EXTERNAL)).start(PORT);

        if (watcher) {
            new Build().enableFileWatcher(Paths.get(site).toAbsolutePath());
        }
    }
}