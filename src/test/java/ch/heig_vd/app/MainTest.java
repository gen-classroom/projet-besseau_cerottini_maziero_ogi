package ch.heig_vd.app;

import static org.junit.Assert.assertEquals;
import picocli.CommandLine;
import org.junit.Test;

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
        int result = new CommandLine(new Main()).execute("statique", "-v");
        int result2 = new CommandLine(new Main()).execute("statique", "--version");
        assertEquals(0, result + result2);
    }
}