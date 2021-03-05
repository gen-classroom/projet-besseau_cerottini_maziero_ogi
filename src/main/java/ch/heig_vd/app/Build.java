package ch.heig_vd.app;

import picocli.CommandLine;

@CommandLine.Command(name = "Build")
class Build implements Runnable{
    public void run(){
        System.out.println("All good, executing Build");
    }
}