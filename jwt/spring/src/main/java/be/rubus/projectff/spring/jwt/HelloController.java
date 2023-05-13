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
package be.rubus.projectff.spring.jwt;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class HelloController {

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('administrator')")
    public String getAdminMessage() {
        return "Protected Resource; Administrator Only ";
    }

    @GetMapping("/user")
    // No @PreAuthorize, so only valid JWT required
    public String getUser(JwtAuthenticationToken authentication) {
        Object username = authentication.getTokenAttributes().get("preferred_username");
        return "Hello " + username.toString();
    }

}
