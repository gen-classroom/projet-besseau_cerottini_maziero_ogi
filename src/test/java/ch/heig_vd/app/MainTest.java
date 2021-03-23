package ch.heig_vd.app;

import picocli.CommandLine;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MainTest
{
    /**
     * Tests the -v and --version options
     */
    @Test
    public void shouldDisplayTheCurrentVersion()
    {
        // Creates a stream to hold the output
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        PrintStream sysOut = System.out;
        System.setOut(ps);

        // Tests the first command
        int result = new CommandLine(new Main()).execute("statique", "-v");
        assertFalse(os.toString().isEmpty());
        String s = os.toString();
        System.out.flush();

        // Tests the second command
        int result2 = new CommandLine(new Main()).execute("statique", "--version");
        assertFalse(os.toString().isEmpty());
        System.out.flush();

        // Resets the system output
        System.setOut(sysOut);

        // Checks no error was found during the commands execution
        assertEquals(0, result + result2);
    }
}