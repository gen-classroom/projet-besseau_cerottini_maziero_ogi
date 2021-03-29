package ch.heig_vd.app.command;

import picocli.CommandLine;

@CommandLine.Command(name = "serve",
        description = "Serve a static site")
public class Serve implements Runnable{
    @CommandLine.Parameters(description = "Path of site to serve.")
    public void run(){
        System.out.println("All good, executing Serve");
    }
}