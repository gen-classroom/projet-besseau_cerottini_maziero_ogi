package ch.heig_vd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "gen")
public class Main implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Please use one of the subcommands: 'new', 'clean', 'build' or 'serve'");
        return 0;
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new Main()).execute(args);
        System.exit(rc);
    }

}