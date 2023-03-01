/*
 * Copyright 2023 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.rubus.projectff.startup.demo.framework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractHandler implements HttpHandler {

    private final List<String> supportedMethods;

    public AbstractHandler(String... supportedMethods) {
        this.supportedMethods = Arrays.asList(supportedMethods);
    }

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!supportedMethods.contains(requestMethod)) {
            sendResponse(exchange, ServerResponse.MethodNotAllowed());
        }
        switch (requestMethod) {
            case "GET" -> sendResponse(exchange, performGet(exchange));
            case "POST" -> sendResponse(exchange, performPost(exchange));
            default -> sendResponse(exchange, ServerResponse.NotImplemented());
        }
    }

    private void sendResponse(HttpExchange exchange, ServerResponse serverResponse) throws IOException {
        int statusCode = serverResponse.getStatusCode();
        if (statusCode >= 300) {
            exchange.sendResponseHeaders(statusCode, -1);
        }
        // Set the response headers and status code
        exchange.getResponseHeaders().set("Content-Type", serverResponse.getContentType());
        exchange.sendResponseHeaders(200, serverResponse.getMessage().length);

        // Write the response message to the output stream
        OutputStream responseStream = exchange.getResponseBody();
        responseStream.write(serverResponse.getMessage());
        responseStream.close();
    }

    public ServerResponse performGet(HttpExchange exchange) {
        return ServerResponse.NotImplemented();
    }

    public ServerResponse performPost(HttpExchange exchange) throws IOException {
        return ServerResponse.NotImplemented();
    }

    protected String getRequestBody(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        byte[] requestBodyBytes = requestBody.readAllBytes();
        return new String(requestBodyBytes, StandardCharsets.UTF_8);
    }
}
