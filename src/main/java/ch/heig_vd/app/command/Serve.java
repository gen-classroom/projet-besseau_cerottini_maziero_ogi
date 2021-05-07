package ch.heig_vd.app.command;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import picocli.CommandLine;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

            //check if filepath start with '/'
            Path path = Paths.get(filePath, "/build").normalize().toAbsolutePath();
            if (!path.toFile().exists()) {
                throw new IllegalArgumentException("Directory does not exists");
            }
            filePath = path.toString();
            httpServer.createContext("/", new MyHandler());
            httpServer.start(); //to start server
            while (true) ;
            //httpServer.stop(0); //to stop the server

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All good, executing Serve");
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            URI uri = ex.getRequestURI();
            String name = new File(uri.getPath()).getName();
            File path = new File(filePath, name);

            Headers h = ex.getResponseHeaders();
            // Could be more clever about the content type based on the filename here.
            h.add("Content-Type", "text/html");

            OutputStream out = ex.getResponseBody();

            if (path.exists()) {
                ex.sendResponseHeaders(200, path.length());
                out.write(Files.readAllBytes(path.toPath()));
            } else {
                System.err.println("File not found: " + path.getAbsolutePath());

                ex.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
            }

            out.close();
        }
    }
}