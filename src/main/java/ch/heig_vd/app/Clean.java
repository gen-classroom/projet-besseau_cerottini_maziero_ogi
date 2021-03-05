package ch.heig_vd.app;

import picocli.CommandLine;

@CommandLine.Command(name = "Clean")
class Clean implements Runnable{
    public void run(){
        System.out.println("All good, executing Clean");
    }
}