# JWT Support in Ktor

Use the JWT token for authentication and authorization similar to other frameworks.

## Setup

Following steps are required to set it up.

- Make sure you have the dependency for authentication and JWT support added to your project.
````
    <dependency>
        <groupId>io.ktor</groupId>
        <artifactId>ktor-server-auth-jvm</artifactId>
        <version>${ktor_version}</version>
    </dependency>

    <dependency>
        <groupId>io.ktor</groupId>
        <artifactId>ktor-server-auth-jwt-jvm</artifactId>
        <version>${ktor_version}</version>
    </dependency>
````
- Make sure you configure the JWT authentication using a construct like
````
     authentication {
        jwt("jwt-auth") {
```` 
This requires to define a JWT verifier and what needs to be checked, like the issuer and audience. Have a look into the _Security_kt_ for the details.
- The expected issuer and audience claim values are defined within the _application.yaml_ file.

## Improvements

This example also has 2 improvements regarding using JWT token in Ktor.

First of all there is a class that retrieves the public key for the id which is found within the header of the JWT.  If we present a token, the class is responsible for providing the key that can be used for the verification. if it is not know, it loads the JWKS from the configured URL of keycloak and checks if the key is a valid one. See `PublicKeyCache`.

Secondly, there is no generic way of checking in Ktor if the JWT token contains certain roles that are required before the client is allowed to make the request.  You can do this within the lambda which provides the functionality for the request by checking the parsed token.
The example has a generic function that combines the JWT validation and the check if tghe token has a set of Roles. See the `authorizeWithRoles` extension function ion `Route` defined within the file _Security.kt_.

## Usage

Instead of just defining the route and the functionality associated with it

````
    get("/hello") {
        call.respondText("Hello world")
    }
````
You need to wrap it with the authentication that needs to be performed.  When you only need a valid JWT, so no role checks, you can use the standard Ktor way

````
    authenticate("jwt-auth") {
        get("/protected/user") {
            val principal = call.authentication.principal<JWTPrincipal>()
            val username = principal?.payload?.getClaim("preferred_username")?.asString()
            call.respondText("Hello, $username!")
        }
    }
````

Using the custom created extension function, you can also check if one or more roles are available within the token.

````
        authorizeWithRoles("jwt-auth", listOf("administrator")) {
            get("/protected/admin") {
                call.respondText("Protected Resource; Administrator Only ")
            }
        }
````
