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
package be.rubus.projectff.startup.demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HelloServer {
    public static void main(String[] args) throws Exception {
        // Create a new HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 128);

        // Set up a handler for the "/hello/{name}" endpoint
        server.createContext("/hello/", new HelloHandler());

        // Set up a handler for the "/person" endpoint
        server.createContext("/person", new PersonHandler());

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                System.out.println("Generic 404 response for " + exchange.getRequestURI().getPath());
                exchange.sendResponseHeaders(404, -1);
            }
        });

        // Start the server
        server.start();

        // Output a message to the console to indicate that the server has started
        System.out.println("Server started on port 8080");
    }
}
