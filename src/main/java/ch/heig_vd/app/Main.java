package ch.heig_vd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "gen", mixinStandardHelpOptions = true)
public class Main implements Callable<Integer> {

    @Parameters(index = "0", description = "The command to execute") private String param;


    @Override
    public Integer call() {
        switch (param){
            case "clean":
                break;
            case "build":
                break;
            case "new":
                break;
            case "serve":
                break;
            default: System.out.println("Please use one of the subcommands: 'new', 'clean', 'build' or 'serve'");
        }


        return 0;
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new Main()).execute(args);
        System.exit(rc);
    }

}