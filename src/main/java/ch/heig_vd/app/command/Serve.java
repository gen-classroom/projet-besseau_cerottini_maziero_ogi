package ch.heig_vd.app.command;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CommandLine.Command(name = "serve",
        description = "Serve a static site")
public class Serve implements Runnable {
    @CommandLine.Parameters(description = "Path of site to serve.")
    private static String filePath;
    private static final int PORT = 8080;

    public void run() {
        try {
            Path path = Paths.get(filePath, "/build").normalize().toAbsolutePath();
            if (!path.toFile().exists()) {
                throw new IllegalArgumentException("Directory does not exists");
            }
            filePath = path.toString();

            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            httpServer.createContext("/", new MyHandler());
            httpServer.start(); //to start server
            while (true) ;
            //httpServer.stop(0); //to stop the server

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            URI uri = ex.getRequestURI();
            String name = new File(uri.getPath()).getName();
            System.out.println(uri.getPath());
            File path = new File(filePath, uri.getPath());
            System.out.println("Asking "+name);
            Headers h = ex.getResponseHeaders();

            h.add("Content-Type", getContentType(FilenameUtils.getExtension(name)));

            try(OutputStream out = ex.getResponseBody()) {
                if (path.exists()) {
                    ex.sendResponseHeaders(200, path.length());
                    out.write(Files.readAllBytes(path.toPath()));
                } else {
                    System.err.println("File not found: " + path.getAbsolutePath());
                    ex.sendResponseHeaders(404, 0);
                    out.write("404 File not found.".getBytes());
                }
            }catch (IOException e){
                System.err.println("An error occurred while serving a file. "+e);
            }
        }

        private String getContentType(String extension){
            switch (extension){
                case "png":
                    return "image/png";
                case "jpg":
                    return "image/jpg";
                case "svg":
                    return "image/svg+xml";
                case "gif":
                    return "image/gif";
                case "mp3":
                    return "audio/mp3";
                default:
                    return "text/html";
            }
        }
    }
}