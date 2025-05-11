package com.example.otp.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class SafeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            handleRequest(exchange);
        } catch (Exception e) {
            e.printStackTrace(); // для логирования в консоль
            String response = "Internal Server Error: " + e.getMessage();
            exchange.sendResponseHeaders(500, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            exchange.close();
        }
    }

    public abstract void handleRequest(HttpExchange exchange) throws IOException;
}
