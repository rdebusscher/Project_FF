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
package be.atbash.projectff.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import be.atbash.projectff.ktor.plugins.configureRouting
import be.atbash.projectff.ktor.plugins.configureSerialization

fun main() {
    // Configuration within code
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()  // Support for JSON
    configureRouting()  // Define the routes
}
