package ch.heig_vd.app.errorHandler;

import picocli.CommandLine;

/**
 *
 * Display a nice error message to the end user
 *
 * @author Besseau Léonard
 */
public class PrintExceptionMessageHandler implements CommandLine.IExecutionExceptionHandler {
    public int handleExecutionException(Exception ex,
                                        CommandLine cmd,
                                        CommandLine.ParseResult parseResult) {

        // bold red error message
        cmd.getErr().println(cmd.getColorScheme().errorText(ex.getMessage()));

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : cmd.getCommandSpec().exitCodeOnExecutionException();
    }
}