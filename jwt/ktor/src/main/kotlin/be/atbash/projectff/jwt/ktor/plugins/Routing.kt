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
package be.atbash.projectff.jwt.ktor.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        authorizeWithRoles("jwt-auth", listOf("administrator")) {
            get("/protected/admin") {
                call.respondText("Protected Resource; Administrator Only ")
            }
        }

        authenticate("jwt-auth") {
            get("/protected/user") {
                val principal = call.authentication.principal<JWTPrincipal>()
                //val username = principal?.payload?.getClaim("username")?.asString()
                val username = principal?.payload?.getClaim("preferred_username")?.asString()
                call.respondText("Hello, $username!")
            }
        }

    }
}
