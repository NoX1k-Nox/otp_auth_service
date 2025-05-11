package com.example.otp.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ApiResponseUtils {

    public static void sendOk(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, 200, response);
    }

    public static void sendCreated(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, 201, response);
    }

    public static void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, 400, "{\"error\": \"" + message + "\"}");
    }

    public static void sendUnauthorized(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, 401, "{\"error\": \"" + message + "\"}");
    }

    public static void sendForbidden(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, 403, "{\"error\": \"" + message + "\"}");
    }

    public static void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, 404, "{\"error\": \"" + message + "\"}");
    }

    public static void sendInternalServerError(HttpExchange exchange, String message) throws IOException {
        sendResponse(exchange, 500, "{\"error\": \"" + message + "\"}");
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
