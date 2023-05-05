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
package be.atbash.projectff.jwt.payara;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.Claim;

/**
 *
 */
@Path("/protected")
@RequestScoped
public class ProtectedController {

    @Inject
    @Claim("preferred_username")
    private String name;

    /*
    @Inject
    private JsonWebToken jsonWebToken;
    // When you need access to every aspect of the JWT token.
     */

    @GET
    @Path("/admin")
    @RolesAllowed("administrator")
    public String getJWTBasedValue() {
        return "Protected Resource; Administrator Only ";
    }

    @GET
    @Path("/user")
    // No roles specified, so only valid JWT is required
    public String getUser() {
        return "Protected Resource; user : " + name;
    }

}
