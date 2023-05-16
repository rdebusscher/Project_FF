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

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.net.URL

fun Application.configureSecurity() {

    authentication {
        jwt("jwt-auth") {
            realm = "Atbash project FF"
            // this@configureSecurity refers to Application.configureSecurity()
            val issuer = this@configureSecurity.environment.config.property("jwt.issuer").getString()
            val expectedAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            val jwkUrl = URL("$issuer/protocol/openid-connect/certs")
            val jwkProvider = UrlJwkProvider(jwkUrl)

            verifier {
                val publicKey = PublicKeyCache.getPublicKey(jwkProvider, it)

                JWT.require(Algorithm.RSA256(publicKey, null))
                    .withAudience(expectedAudience)
                    .withIssuer(issuer)
                    .build()

            }

            validate { credential ->
                // If we need validation of the roles, use authorizeWithRoles
                // We cannot define the roles that we need to be able to check this here.
                JWTPrincipal(credential.payload)
            }

            challenge { defaultScheme, realm ->
                // Response when verification fails
                // Ideally should be a JSON payload that we sent back
                call.respond(HttpStatusCode.Unauthorized, "$realm: Token is not valid or has expired")
            }
        }
    }

}

fun Route.authorizeWithRoles(
    realm: String,
    roles: List<String>,
    block: Route.() -> Unit
) {
    authenticate(realm) {
        // Do the standard authenticate

        // We add a custom phase in the Pipeline, just before the Call (=user defined logic for the URL)
        // Where we check if the JWT has the requested roles as a claim
        val roleCheckPhase = PipelinePhase("RoleCheckPhase")
        insertPhaseBefore(ApplicationCallPipeline.Call, roleCheckPhase)
        intercept(roleCheckPhase) {
            val principal = call.authentication.principal<JWTPrincipal>()
            // "groups" claim should be made configurable.
            val claimRoles = principal?.payload?.getClaim("groups")?.asList(String::class.java) ?: emptyList()
            // Dos the JWT has all th required roles?
            if (claimRoles.containsAll(roles)) {
                // Call the lambda defined within the route indication.
                proceed()
            } else {
                call.respond(HttpStatusCode.Forbidden)
                finish()  // Stop the pipeline
            }
        }
        // This registers the get(), post(), .... inside as route.
        block()
    }
}

