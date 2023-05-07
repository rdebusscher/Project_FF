# JWT Support in Payara Micro

Is using the JWT Auth part of the MicroProfile Specification.

## Setup

Following steps are required to set it up.

- Make sure you have the MicroProfile dependency defined in your project. In this case I used the umbrella specification, but for the JWT support, the JWT Auth API would be enough. 
````
<dependency>
    <groupId>org.eclipse.microprofile.jwt</groupId>
    <artifactId>microprofile-jwt-auth-api</artifactId>
    <version>2.1</version>
    <scope>provided</scope>
</dependency>
````
- Configure the public key for verification, issuer, and optionally the audience. These values are picked up through MicroProfile Config and in this example, we use the `microprofile-config.properties` file. We are using Keycloak, so we define the URL where the keys can be retrieved in JWS format using the _mp.jwt.verify.publickey.location_ key.  The issuer is equally to the realm name (in th URL format for keycloak).
- Don't forget to add the `@LoginConfig` annotation to a CDI bean in order to 'activate' the JWT support. In this example, it is placed on the class that defines the JAX-RS application (see class `JwtRestApplication`)
- For Payara Micro, it is also required to define the possible _roles_ we are expected and this is possible through the `@DeclareRoles` annotation, place in this case on the same class definition.

## Usage

On the JAX-RS methods, we can define the role that we expect in the JWT before the client is allowed to make the request.  In the example, there is an endpoint that can ony be called with the _administrator_ role.

````
@RolesAllowed("administrator")
````

When no `@RolesAllowed` annotation is present, than only the JWT needs to be valid in order to be callable.

You can access a single claim value from the token by using the following property definition

````
    @Inject
    @Claim("preferred_username")
    private String name;
````

or you can have access to any aspect of the token by accessing the `JsonWebToken`. 

````
    @Inject
    private JsonWebToken jsonWebToken;
````
