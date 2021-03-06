package ch.heig_vd.app.command;

import ch.heig_vd.app.Main;
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
        int result = new CommandLine(new Main()).execute("-v");
        assertEquals("Statique", os.toString().substring(0,8));
        System.out.flush();

        // Tests the second command
        int result2 = new CommandLine(new Main()).execute("--version");
        assertEquals("Statique", os.toString().substring(0,8));
        System.out.flush();

        // Resets the system output
        System.setOut(sysOut);

        // Checks no error was found during the commands execution
        assertEquals(0, result + result2);
    }
}