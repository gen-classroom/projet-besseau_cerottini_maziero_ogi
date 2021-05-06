package ch.heig_vd.app.command;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import picocli.CommandLine;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@CommandLine.Command(name = "serve",
        description = "Serve a static site")
public class Serve implements Runnable{
    @CommandLine.Parameters(description = "Path of site to serve.")
    private static String filePath;
    private static final int PORT = 8080;

    public void run() {

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

            //check if filepath start with '/'
            String path = filePath;
            if (!filePath.startsWith("/")) {
                StringBuilder sb = new StringBuilder(filePath);
                sb.insert(0, "/");
                path = sb.toString();
            }

            httpServer.createContext(path, new MyHandler());
            httpServer.start(); //to start server
            while(true);
            //httpServer.stop(0); //to stop the server

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All good, executing Serve");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            t.getResponseHeaders().set("Content-Type","text/html");

            String line;
            StringBuilder resp = new StringBuilder();

            //check if filepath start with '/'
            // TODO : Redundant code
            String pathTest = filePath;
            if (filePath.startsWith("/")) {
                pathTest = pathTest.substring(1);
            }

            String uri = t.getRequestURI().toString().substring(1);

            try {
                File newFile;
                if (uri.equals(pathTest) || uri.equals(pathTest + "/"))
                    newFile = new File(Paths.get(pathTest).normalize().toAbsolutePath() + "/build/index.html");
                else {
                    if(!uri.endsWith(".html")){
                        throw new IOException("not html file");
                    }
                        newFile = new File(Paths.get(uri).normalize().toAbsolutePath().toString());
                }

                System.out.println("*****lecture du fichier*****");
                System.out.println("nom du fichier: " + newFile.getName());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    resp.append(line);
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                t.sendResponseHeaders(404, resp.length());
            }

            t.sendResponseHeaders(200, resp.length());
            OutputStream os = t.getResponseBody();
            os.write(resp.toString().getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}