package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        int port = 8080;
        while (true) {
            try {
                SpringApplication app = new SpringApplication(Main.class);
                app.setDefaultProperties(java.util.Map.of("server.port", port));
                app.run(args);
                System.out.println("Server started on port " + port);
                break; // success, exit loop
            } catch (WebServerException e) {
                System.out.println("Port " + port + " is in use, trying next...");
                port++; // try next port
            }
        }
    }
}