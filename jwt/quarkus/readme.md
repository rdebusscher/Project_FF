# JWT Support in Quarkus

Is using the following JWT Auth specification of the MicroProfile Specification.

## Setup

Following steps are required to set it up.

- Make sure you have the required SmallRye JWT artifacts included in your application. We need the main artifact and the build one if you are using Graal VM (not the case in our example). 
````
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-jwt</artifactId>
    </dependency>
````
- Configure the public key for verification, issuer, and optionally the audience. These values are picked up through MicroProfile Config and in this example, we use the `application.properties` file. We are using Keycloak, so we define the URL where the keys can be retrieved in JWS format using the _mp.jwt.verify.publickey.location_ key.  The issuer is equally to the realm name (in th URL format for keycloak).

## Usage

On the REST methods, we can define the role that we expect in the JWT before the client is allowed to make the request.  In the example, there is an endpoint that can ony be called with the _administrator_ role.

````
@RolesAllowed("administrator")
````

If you do not require any specific role to be present before the endpoint can be called, a valid JWT is enough, you can indicate this by using the _@Authenticated_ whicj is a Smallrye JWT specific class.  

````
@Authenticated
````

You can access a single claim value from the token by using the following property definition

````
    @Inject
    @Claim("preferred_username")
    String name;
````

or you can have access to any aspect of the token by accessing the `JsonWebToken`.

````
    @Inject
    JsonWebToken jsonWebToken;
````
