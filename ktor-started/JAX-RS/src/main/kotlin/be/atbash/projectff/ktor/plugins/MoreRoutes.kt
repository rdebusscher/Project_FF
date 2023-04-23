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
package be.atbash.projectff.ktor.plugins

import be.atbash.projectff.ktor.model.Person
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

fun Route.otherRoutes() {
    route("/person") {
        get {
            // You can return any Kotlin instance because serialization-kotlinx-json is configured
            call.respond(Person("rudy", 42))
        }
        get("/jsonp") {
            // Builder pattern of serialization-kotlinx-json that is similar to JSON-P
            val result = buildJsonObject {
                put("type", "json")
                put("age", 42)
                put("name", "Rudy")
            }
            call.respond(result)
        }
        post {
            // This statement converts automatically from JSON to Kotlin instance.
            val person = call.receive<Person>()
            call.respondText("POST received with name ${person.name} and age ${person.age}")
        }
    }
}