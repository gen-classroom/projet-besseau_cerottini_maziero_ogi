package benchmark;

import ch.heig_vd.app.command.Build;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.File;
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class Bench{
    String path = "./benchmark/example";
    File filesDirectory;
    File configFile;
    File buildDirectory;
    File templateFolder;
    @Setup
    public void setup(){
        filesDirectory = new File(path.toString()); //get all directory from there
        configFile = new File(path + "/config.json");
        buildDirectory = new File(path + "/build"); //build new directory
        buildDirectory.mkdir();
        templateFolder = new File(path + "/template");;
    }

    @Benchmark
    public void test() {
        new Build().build(filesDirectory, configFile, buildDirectory, templateFolder);
    }
}
