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
package be.rubus.projectff.startup.demo.helidon.json;

import io.helidon.common.reactive.Single;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class PersonService implements Service {
    @Override
    public void update(Routing.Rules rules) {
        rules
                .get("/", this::getPerson)
                .post("/", this::handlePersonRequest);

    }

    public void getPerson(ServerRequest request, ServerResponse response) {
        response.send(new Person("Helidon", 42));
    }


    public void handlePersonRequest(ServerRequest request, ServerResponse response) {
        request.content().as(Person.class)
                .thenAccept(p -> response.send(p.toString()));
    }
}
