# The FF Project

FF or the Framework Frenzy, Framework Face-off, Framework Fireworks, or ... the comparison and similarities between Jakarta EE Web Profile, Spring Boot, Quarkus, Kotlin Ktor, and a few others regarding creating backend applications with REST endpoints.


# Start up

A comparison between the start up time (time until first request is handled), memory usage and application size of various frameworks.  The application had 3 endpoints, a hello world endpoint returning a String, an endpoint return a JSON string serialised from an object instance, and a POST endpoint processing a JSON (and returning a String)

Data gathering is done using a Python script.

See the directory _startup-time_ readme for the details.

Frameworks

- Spring Boot 3
- Quarkus 2.16
- Ktor 
- HttpServer from Java
 