package ch.heig_vd.app.command;

import ch.heig_vd.app.command.utils.ExitHandler;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Command to enable a web server used to visualize files in the build folder
 * @author Cerottini Alexandra
 * @author Ogi Nicolas
 *
 */
@CommandLine.Command(name = "serve",
        description = "Serve a static site")
public class Serve implements Runnable {
    @CommandLine.Parameters(description = "Path of site to serve.")
    String site;
    @CommandLine.Option(names = {"-w", "--watcher"}, paramLabel = "Watcher", description = "Enable file watcher to automate")
    boolean watcher;

    private static final int PORT = 8080;

    /**
     * Method used to run the command serve and execute a small HTTP server to visualize files in the build folder
     */
    public void run() {

        // Serve the site
        Javalin.create(config ->
                config.addStaticFiles(Paths.get(site).resolve("build").toAbsolutePath().toString(), Location.EXTERNAL)).start(PORT);

        if (watcher) {
            new Build().enableFileWatcher(Paths.get(site).toAbsolutePath());
        }

        ExitHandler.awaitSignal(watcher);
    }
}