# JWT Support in Atbash Runtime

Is using the JWT Auth part of the MicroProfile Specification.

## Setup

The configuration of your project is different if you will use the Jakarta Runner functionality of Atbash to run your application, or if you use your application as a WAR file which is executed by the Atbash Runtime.

````
java -jar <myapp.jar> 
java -jar atbash-runtime.jar -m +jwt-auth <myapp.war>
````
The first command is using the Jakarta Runner functionality, the second one is the WAR application executed by Atbash Runtime where you need to specify that the JWT Auth module should be activated.

This demo uses the Jakarta Runner functionality, and requires a bit more configuration (but can be used to run the app from within the IDE!)

- Add the JWT Authentication module of Atbash Runtime to ypour Maven project.
````
<dependency>
    <groupId>be.atbash.runtime</groupId>
    <artifactId>jwt-auth-module</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
````

Also make sure you add the Core API dependency so that you have all functionality of th Jakarta EE 10 Core profile available like JAX-RS.  The usage of the runner also requires additional dependencies.

````
<dependency>
    <groupId>be.atbash.runtime.api</groupId>
    <artifactId>core-api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>be.atbash.runtime</groupId>
    <artifactId>jakarta-executable</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>be.atbash.runtime</groupId>
    <artifactId>jakarta-executable-impl</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <!-- only needed at runtime, but with executable within IDE not able to exclude it -->
</dependency> 
````

You also need to indicate that you want the JWT Auth Module to be active (since there is no scanning of the WAR file and thus cannot find out it has `@LoginConfig`defined on a CDI class. )

````
public static void main(String[] args) {

    JakartaSERunnerBuilder.newBuilder(ProtectedController.class)
            .additionalModules(JWTAuthModule.JWT_AUTH_MODULE_NAME)
            .run();
}
````

In case you are building a WAR file, having the MicroProfile JWT authentication API is enough.  

````
<dependency>
    <groupId>org.eclipse.microprofile.jwt</groupId>
    <artifactId>microprofile-jwt-auth-api</artifactId>
    <version>2.1</version>
    <scope>provided</scope>
</dependency>
````
- Configure the public key for verification, issuer, and optionally the audience. These values are picked up through MicroProfile Config and in this example, we use the `microprofile-config.properties` file. We are using Keycloak, so we define the URL where the keys can be retrieved in JWS format using the _mp.jwt.verify.publickey.location_ key.  The issuer is equally to the realm name (in th URL format for keycloak).
- You only need the `@LoginConfig` annotation on a CDI bean in order to 'activate' the JWT support if you are using the WAR solution.
- There is no need to define the possible roles though an annotation `@DeclareRoles`. A role defined on thr JAX-RS method is checked with the JWT token value and authorization is based on a match or lack of the match.

## Usage

On the JAX-RS methods, we can define the role that we expect in the JWT before the client is allowed to make the request.  In the example, there is an endpoint that can ony be called with the _administrator_ role.

````
@RolesAllowed("administrator")
````

When no `@RolesAllowed` annotation is present, than no one is able to call the method. So make sure that you add `@PermitAll` if any request with a valid JWT token is allowed to execute the endpoint.

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
