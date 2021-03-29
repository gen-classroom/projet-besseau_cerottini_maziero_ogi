package ch.heig_vd.app;

import ch.heig_vd.app.command.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

import static java.lang.System.exit;

// Main command info
@Command(name = "statique",
        description = "A brand new static site generator.",
        version = {"@|yellow Statique v1.0|@"},
        subcommands = {Build.class, Serve.class, Clean.class, Init.class},
        synopsisSubcommandLabel = "COMMAND")
public class Main implements Callable<Integer> {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Parameters(index = "0", description = "The command to execute")
    private String param;

    // Option -v to display the program version
    @CommandLine.Option(names = {"-v", "--version"},
            versionHelp = true,
            description = "Displays the current version")
    boolean showVersion;

    public static void main(String... args) {
        if (args.length == 0){
            System.err.println("Invalid number of argument. You should use statique as first argument");
            exit(1);
        }
        // Parses and executes the options
        CommandLine commandLine = new CommandLine(new Main());
        commandLine.parseArgs(args);
        if (commandLine.isVersionHelpRequested()) { // Show version
            commandLine.printVersionHelp(System.out);
            return;
        }

        // Executes the subcommands
        exit(commandLine.execute(args));
    }

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);
        return 0;
    }

}

