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

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/show_uri/{...}") {
            // {...} -> capture anything.
            val uri = call.request.uri
            call.respondText("The complete URI of the call was $uri")
        }

        get("/hello/{name?}") {
            // return@get instructs to end lambda execution after the statement is executed and continue
            // with the statement that called get function.
            val name = call.parameters["name"] ?: return@get call.respond(
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing name parameter"
                )
            )
            val language = call.request.queryParameters["language"] ?: "en"
            call.respondText("Hello $name with language $language")
        }

        get("/is_even_value/{value}") {
            // toIntOrNull -> return null of not an integer.
            val value: Int = call.parameters["value"]?.toIntOrNull() ?: return@get call.respond(
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing value parameter"
                )
            )
            if (value % 2 == 0) {
                call.respondText("Value is a correct even number")
            } else {
                call.respond(HttpStatusCode.NotAcceptable)
            }
        }

        // You can group routes according to 'endpoint'
        otherRoutes()
    }
}
