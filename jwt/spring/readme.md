# JWT Support in Spring Boot

The support is provided through the artifact `spring-boot-starter-oauth2-resource-server`. This example uses Spring Boot 3 and is running on JDK 17.

## Setup

Following steps are required to set it up.

- Make sure you have the OAuth2 Resource Server dependency defined in your project. 
````
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>		
````
- Configure the URL for the configuration retrieval, and optionally the audience. These values are picked up through Spring Boot config and here defined in this example within the _application.properties_ file. We are using Keycloak, so we define the URL where the config can be retrieved which includes the URL for key retrieval in JWS format and issuer value using the _spring.security.oauth2.resourceserver.jwt.issuer-uri_ key.
- Define a `JwtAuthenticationConverter` Bean that defines how the roles are retrieved from the JWT token. In the example there is a method annotated with `@Bean` that retrieves them from the _groups_ claim and makes them available within spring without a prefix.
- Define in the `WebSecurityConfigurerAdapter` which roles are required for each (group) of endpoints. Or activate the Method Security which allows to define the role in a fine grained manner on the method itself as an annotation.

## Usage

On the REST methods, we can define the role that we expect in the JWT before the client is allowed to make the request.  In the example, there is an endpoint that can ony be called with the _administrator_ role.

````
@PreAuthorize("hasAuthority('administrator')")
````

When no `@PreAuthorize` annotation is present, than only the JWT needs to be valid in order to be callable.

You can access a claim value from the token by using a `JwtAuthenticationToken` as method parameter.

````
    public String getUser(JwtAuthenticationToken authentication) {
        Object username = authentication.getTokenAttributes().get("preferred_username");
        return "Hello " + username.toString();
    }
````
