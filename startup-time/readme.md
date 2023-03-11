
# Comparison

Comparison of the 
- startup Time, time until first request is processed by runtime
- memory usage
- package size

Of the following frameworks

- Spring Boot 3.0. Project created using Spring Initializr using Spring Reactive web to make use of Netty.
- Quarkus 2.16. Project created using code.quarkus.io using RestEasy Reactive and RestEasy Reactive JSONB.
- Ktor 2.2. Project created using IntelliJ project wizard using Netty server, Content negotiation and Kotlin JSON serialisation.
- HttpServer included in JVM with the addition of Atbash Octopus JSON 1.1.1 for the JSON Binding support. Custom minimal framework for handling requests and sending responses.

- Payara 6. Jakarta EE 10 application running on Payara 6
- OpenLiberty. Jakarta EE 8 application running on OpenLiberty.
- Wildfly 27. Jakarta EE 10 application running on Wildfly
- Helidon SE 3.1. Project created using the Helidon Starter the includes JSON-P and JSON-B support.
- Piranha. Jakarta EE 10 core profile application running on the Cor profile distribution
- Micronaut 3.8. Project created using Micronaut Launcher and has support for JSON handling.

Each framework has the dependencies to handle REST requests using JSON objects and is running on JDK 17.


# Preparation

Run the following commands in each directory

springboot : `mvn clean package`
quarkus : `mvn clean package -Dquarkus.package.type=uber-jar`
ktor : `mvn clean package`
payara: `mvn clean package`
liberty: `mvn clean package`
wildfly: `mvn clean package`
helidon: `mvn clean package`
piranha: `mvn clean package`. Also perform the action in the bin/readme.md to be able to test it!
micronaut: `mvn clean package`



Within the _scripts_ directory, prepare the python environment

- `python3 -m venv ./venv` to create a virtual environment
- `source venv/bin/activate` to activate the virtual environment
- `python -m pip install requests` install package

# Execute

From within the _scripts_ directory with the Python virtual environment native, you can run following scripts.


- `python test_runtime.py -r PureJava -n 10`
- `python test_runtime.py -r Ktor -n 10`
- `python test_runtime.py -r Quarkus -n 10`
- `python test_runtime.py -r SpringBoot -n 10`

- `python test_runtime.py -r Payara -n 10`
- `python test_runtime.py -r Liberty -n 10`
- `python test_runtime.py -r Wildfly -n 10`
- `python test_runtime.py -r Helidon -n 10`
- `python test_runtime.py -r Piranha -n 10`
- `python test_runtime.py -r Micronaut -n 10`