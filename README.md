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
- Payara 6
- OpenLiberty
- Wildly 27
- Helidon 3.1
- Piranha
- Micronaut



# Ktor for Jakarta EE Developers

A series of examples with Kotlin Ktor for the Jakarta EE developers.

See also blogs : *Coming soon*

# Equivalent of JAX-RS, JSON-B and JSON-P

Example of defining routing and using JSON payloads utilizing similar functionality as JSON-B and JSON-P.

See also blog : *Coming soon*