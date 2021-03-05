package ch.heig_vd.app;
import picocli.CommandLine;

@CommandLine.Command(name = "New")
class New implements Runnable{
    public void run(){
        System.out.println("All good, executing Build");
    }
}
