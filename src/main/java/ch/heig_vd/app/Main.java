package ch.heig_vd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "gen", subcommands = {Build.class, New.class, Serve.class, Clean.class},
        synopsisSubcommandLabel = "COMMAND")
public class Main implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Parameters(index = "0", description = "The command to execute")
    private String param;


    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    public static void main(String... args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }

}

