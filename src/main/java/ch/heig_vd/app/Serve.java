package ch.heig_vd.app;

import picocli.CommandLine;

@CommandLine.Command(name = "Serve")
class Serve implements Runnable{
    public void run(){
        System.out.println("All good, executing Serve");
    }
}