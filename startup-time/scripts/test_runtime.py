import shutil

import requests
import os
from globals import verbose_output
from parameters import get_program_arguments
from run_command import AtbashPerformanceRunner


def get_string_url_call():
    full_url = "GET http://localhost:8080/hello/Testing"
    return full_url, requests.get("http://localhost:8080/hello/Testing")


def get_json_url_call():
    full_url = "GET http://localhost:8080/person"
    headers = {'Accept': 'application/json'}
    return full_url, requests.get("http://localhost:8080/person", headers=headers)


def post_json_url_call():
    full_url = "POST http://localhost:8080/person"
    headers = {'Content-type': 'application/json'}
    return full_url, requests.post("http://localhost:8080/person", json={"name": "John", "age": 42}, headers=headers)


def cleanup_noop():
    pass


def cleanup_piranha():
    os.remove("RUNTIME.pid")
    shutil.rmtree(os.path.abspath("../piranha/bin/ROOT"))


def determine_url_call(url_name):
    if url_name == "GET-String":
        return get_string_url_call
    if url_name == "GET_JSON":
        return get_json_url_call
    if url_name == "POST_JSON":
        return post_json_url_call


def get_execution_parameters(args):
    execution = {"repeat": args.n if not args.v else 1, "v": args.v}
    execution["cleanup"] = cleanup_noop
    if args.runtime == "SpringBoot":
        execution["jar"] = "../springboot/target/spring.jar"
    if args.runtime == "PureJava":
        execution["jar"] = "../java-http/target/java-http.jar"
    if args.runtime == "Quarkus":
        execution["jar"] = "../quarkus/target/quarkus-runner.jar"
    if args.runtime == "Ktor":
        execution["jar"] = "../ktor/target/ktor-jar-with-dependencies.jar"
    if args.runtime == "Payara":
        execution["jar"] = "../payara/target/payara-microbundle.jar"
    if args.runtime == "Liberty":
        execution["jar"] = "../liberty/target/liberty.jar"
    if args.runtime == "Wildfly":
        execution["jar"] = "../wildfly/target/wildfly-bootable.jar"
    if args.runtime == "Helidon":
        execution["jar"] = "../helidon/target/helidon.jar"
    if args.runtime == "Micronaut":
        execution["jar"] = "../micronaut/target/micronaut-0.1.jar"
    if args.runtime == "Atbash":
        execution["jar"] = "../Atbash/target/atbash.jar"
    if args.runtime == "Piranha":
        execution["script"] = "./../piranha/bin/piranha.sh"
        execution["cleanup"] = cleanup_piranha

    execution["url_callback"] = determine_url_call(args.url)
    execution["verbose"] = args.v
    return execution


def perform_single_run(execution_parameters):
    runner = AtbashPerformanceRunner()
    if "jar" in execution_parameters:
        runner.launch_runtime_java(execution_parameters["jar"], jvm_params=["-XX:NativeMemoryTracking=detail"])
    else:
        runner.launch_runtime_shell(execution_parameters["script"])

    healthy = runner.wait_for(execution_parameters)
    if healthy:
        runner.calculate_first_response_delay()
        runner.determine_memory_usage(execution_parameters)
        console_output = runner.end_process()
        if execution_parameters["verbose"]:
            verbose_output.append("Runtime console output")
            verbose_output.append(console_output)
    else:
        print("Process not properly started")
        print(runner.end_process())
    execution_parameters["cleanup"]()  # Perform the cleanup method
    return healthy


if __name__ == '__main__':
    args = get_program_arguments()
    print("{runtime} testing".format(runtime=args.runtime))
    execution_parameters = get_execution_parameters(args)
    for n in range(execution_parameters["repeat"]):
        success = perform_single_run(execution_parameters)
        if not success:
            break

    if execution_parameters["verbose"]:
        for line in verbose_output:
            print(line)
