import argparse


def get_program_arguments():
    parser = argparse.ArgumentParser(
        prog='Testing runtime startup',
        description='Testing the time until the runtime responds to the first request and how much memory is used for that.')

    parser.add_argument("-r", "--runtime", choices=["SpringBoot", "Quarkus", "Ktor", "PureJava"], required=True)
    parser.add_argument("--url", choices=["GET-String", "GET_JSON", "POST_JSON"], default="GET-String")
    parser.add_argument("-n", type=int, default=1)
    parser.add_argument("-v", action='store_true')

    return parser.parse_args()

