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

import be.atbash.json.JSONValue;
import be.rubus.projectff.startup.demo.framework.ServerResponse;
import be.rubus.projectff.startup.demo.framework.AbstractHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PersonHandler extends AbstractHandler {

    public PersonHandler() {
        super("GET", "POST");
    }

    @Override
    public ServerResponse performGet(HttpExchange exchange) {
        return ServerResponse.Ok(new Person("PureJava", 42));
    }

    @Override
    public ServerResponse performPost(HttpExchange exchange) throws IOException {
        String requestBody = getRequestBody(exchange);
        Person person = JSONValue.parse(requestBody, Person.class);
        return ServerResponse.Ok(person.toString());
    }
}
