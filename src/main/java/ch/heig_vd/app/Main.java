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
        version = {"Statique @|yellow v0.0.1|@"},
        descriptionHeading = "%n@|bold,underline Description|@:%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        subcommands = {Build.class, Serve.class, Clean.class, Init.class},
        synopsisSubcommandLabel = "COMMAND")
public class Main implements Callable<Integer> {

    // Option -v to display the program version
    @CommandLine.Option(names = {"-v", "--version"},
            versionHelp = true,
            description = "Displays the current version")
    boolean showVersion;
    private static CommandLine.Help.ColorScheme colorScheme;
    public static void main(String... args) {
        colorScheme = new CommandLine.Help.ColorScheme.Builder()
                .commands    (CommandLine.Help.Ansi.Style.fg_blue)    // combine multiple styles
                .options     (CommandLine.Help.Ansi.Style.fg_yellow)                // yellow foreground color
                .parameters  (CommandLine.Help.Ansi.Style.fg_green)
                .optionParams(CommandLine.Help.Ansi.Style.italic).build();

        // Parses and executes the options
        CommandLine commandLine = new CommandLine(new Main());
        commandLine.parseArgs(args);
        if (commandLine.isVersionHelpRequested()) { // Show version
            commandLine.printVersionHelp(System.out);
            exit(commandLine.getCommandSpec().exitCodeOnSuccess());
        }

        // Executes the subcommands
        try {
            exit(commandLine.setColorScheme(colorScheme).execute(args));
        }catch (CommandLine.ParameterException ex){
            commandLine.getErr().println(ex.getMessage());
            if (!CommandLine.UnmatchedArgumentException.printSuggestions(ex, commandLine.getErr())) {
                ex.getCommandLine().usage(commandLine.getErr());
            }
            exit(commandLine.getCommandSpec().exitCodeOnInvalidInput());
// exception occurred in business logic
        } catch (Exception ex) {
            ex.printStackTrace(commandLine.getErr());
            exit(commandLine.getCommandSpec().exitCodeOnExecutionException());
        }
        exit(commandLine.getCommandSpec().exitCodeOnSuccess());

    }

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out, colorScheme);
        return 0;
    }

}

