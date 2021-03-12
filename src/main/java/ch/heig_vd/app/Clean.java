package ch.heig_vd.app;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

@CommandLine.Command(name = "Clean")
class Clean implements Runnable{
    public void run(){
        try {
            FileUtils.deleteDirectory(new File("./mon/site/build"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}