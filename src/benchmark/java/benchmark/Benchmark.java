package benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


public class Benchmark {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder().forks(1).warmupIterations(2).measurementIterations(4).build();
        new Runner(options).run();
    }
}