package benchmark;

import ch.heig_vd.app.converter.interpreter.TemplateInterpreter;
import ch.heig_vd.app.converter.utils.Metadata;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class BenchmarkTemplateInterpreterWithMeta {
    String rootFolder = "./benchmark";
    String path = rootFolder + "/example";
    TemplateInterpreter interpreter;

    @Param({"1", "2", "4", "8"})
    private int size;
    ArrayList<Metadata> global;
    ArrayList<Metadata> local;
    String input;

    @Setup
    public void setup() throws IOException {
        File templateFolder = new File(path + "/template");
        interpreter = new TemplateInterpreter(templateFolder);
        String a = new String(Files.readAllBytes(Paths.get(rootFolder + "/inputNoMeta")));
        input = a.repeat(Math.max(0, size - 1));
        global = new ArrayList<>();
        local = new ArrayList<>();
        global.add(new Metadata("title", "a"));
        global.add(new Metadata("a", "a"));
        global.add(new Metadata("b", "a"));
        global.add(new Metadata("c", "a"));
        global.add(new Metadata("d", "a"));
        local.add(new Metadata("title", "a"));
        local.add(new Metadata("a", "a"));
        local.add(new Metadata("b", "a"));
        local.add(new Metadata("c", "a"));
        local.add(new Metadata("d", "a"));
    }

    @Benchmark
    public void run(Blackhole blackhole) throws IOException {
        blackhole.consume(interpreter.generate( global, local, input));
    }
}
