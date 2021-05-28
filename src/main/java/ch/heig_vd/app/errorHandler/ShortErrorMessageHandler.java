package ch.heig_vd.app.errorHandler;

import picocli.CommandLine;

import java.io.PrintWriter;

/**
 *
 * Display a short error message to the end user
 *
 * @author Besseau Léonard
 */
public class ShortErrorMessageHandler implements CommandLine.IParameterExceptionHandler {

    /**
     * Display a short message for an exception
     * @param ex the exception to handle
     * @param args the argument of the command line
     * @return the exit code
     */
    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        PrintWriter err = cmd.getErr();
        // if tracing at DEBUG level, show the location of the issue
        if ("DEBUG".equalsIgnoreCase(System.getProperty("picocli.trace"))) {
            err.println(cmd.getColorScheme().stackTraceText(ex));
        }

        err.println(cmd.getColorScheme().errorText(ex.getMessage())); // bold red
        CommandLine.UnmatchedArgumentException.printSuggestions(ex, err);
        err.print(cmd.getHelp().fullSynopsis());

        CommandLine.Model.CommandSpec spec = cmd.getCommandSpec();
        err.printf("Try '%s --help' for more information.%n", spec.qualifiedName());

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : spec.exitCodeOnInvalidInput();
    }
}
