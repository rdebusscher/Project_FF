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
package be.atbash.projectff.jwt.atbash;

import jakarta.annotation.security.PermitAll;
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

    @GET
    @Path("/admin")
    @RolesAllowed("administrator")
    public String getJWTBasedValue() {
        return "Protected Resource; Administrator Only ";
    }

    @GET
    @Path("/user")
    @PermitAll
    // With Atbash you need to explicitly say that any role is allowed. This for the case that
    // when you forget to define an annotation which defaults than to DenyAll
    public String getUser() {
        return "Protected Resource; user : " + name;
    }

}
