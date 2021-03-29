package ch.heig_vd.app.command;

import picocli.CommandLine;

@CommandLine.Command(name = "serve")
public class Serve implements Runnable{
    public void run(){
        System.out.println("All good, executing Serve");
    }
}