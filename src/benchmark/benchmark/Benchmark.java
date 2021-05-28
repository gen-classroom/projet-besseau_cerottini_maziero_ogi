package benchmark;
import ch.heig_vd.app.command.Build;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

import java.io.File;


public class Benchmark {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @org.openjdk.jmh.annotations.Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void test(){
        String path = "./benchmark/example";
        File filesDirectory = new File(path.toString()); //get all directory from there
        File configFile = new File(path+"/config.json");
        File buildDirectory = new File(path + "/build"); //build new directory
        buildDirectory.mkdir();
        File templateFolder = new File(path + "/template");
        new Build().build(filesDirectory, configFile, buildDirectory, templateFolder);
    }
}
