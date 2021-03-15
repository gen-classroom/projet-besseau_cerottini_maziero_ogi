package ch.heig_vd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "statique", subcommands = {Build.class, New.class, Serve.class, Clean.class},
        synopsisSubcommandLabel = "COMMAND")
public class Main {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Parameters(index = "0", description = "The command to execute")
    private String param;

    public static void main(String... args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }

}

