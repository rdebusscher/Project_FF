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

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import io.ktor.http.auth.*
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.ConcurrentHashMap

/**
 * Keeps a cache of the public keys for validation of the JWT signature.
 * When key rotation is configured
 */
object PublicKeyCache {
    private const val BEARER_PREFIX = "Bearer "
    private val publicKeyCache = ConcurrentHashMap<String, RSAPublicKey>()

    fun getPublicKey(jwkProvider: JwkProvider, authHeader : HttpAuthHeader): RSAPublicKey {
        // authHeader.render() -> Bearer ey....
        val jwtToken = JWT.decode(authHeader.render().substring(BEARER_PREFIX.length))
        val keyId = jwtToken.keyId
        return publicKeyCache.computeIfAbsent(keyId) {
            println("retrieving publicKey for $keyId")
            jwkProvider.get(jwtToken.keyId).publicKey as RSAPublicKey
        }
    }
}