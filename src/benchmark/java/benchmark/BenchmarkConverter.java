package benchmark;

import ch.heig_vd.app.converter.Converter;
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

@State(Scope.Benchmark)
public class BenchmarkConverter {
    String rootFolder = "./benchmark";
    String path = rootFolder + "/example";
    Converter converter;
    File inputFile;
    File outputFolder;

    @Setup
    public void setup() throws IOException {
        File templateFolder = new File(path + "/template");
        inputFile = new File(rootFolder + "/inputConverter.md");
        outputFolder = new File(rootFolder + "/outputConverter");
        outputFolder.mkdir();
        converter = new Converter(new File(path + "/config.json"), templateFolder);

    }

    @Benchmark
    public void run(Blackhole blackhole) {
        converter.markdownToHTML(inputFile,rootFolder + "/outputConverter");
    }
}
