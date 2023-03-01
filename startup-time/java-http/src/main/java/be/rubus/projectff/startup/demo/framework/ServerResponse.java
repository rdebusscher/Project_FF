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

import be.atbash.json.JSONValue;

import java.nio.charset.Charset;

public class ServerResponse {

    private final int statusCode;
    private final byte[] message;

    private final String contentType;

    public ServerResponse(int statusCode) {
        // This is assumed to be an error type statusCode.
        this(statusCode, null, null);

    }

    public ServerResponse(String message) {
        this(200, message, "text/plain");
    }

    public ServerResponse(String message, String contentType) {
        this(200, message, contentType);
    }

    public ServerResponse(int statusCode, String message, String contentType) {
        this.statusCode = statusCode;
        this.message = message == null ? null : message.getBytes(Charset.defaultCharset());
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getMessage() {
        return message;
    }

    public String getContentType() {
        return contentType;
    }

    public static ServerResponse NotImplemented() {
        return new ServerResponse(501);
    }

    public static ServerResponse MethodNotAllowed() {
        return new ServerResponse(405);
    }

    public static ServerResponse Ok(String entity) {
        return new ServerResponse(entity);
    }

    public static ServerResponse Ok(Object entity) {
        return new ServerResponse(JSONValue.toJSONString(entity), "application/json");
    }
}
