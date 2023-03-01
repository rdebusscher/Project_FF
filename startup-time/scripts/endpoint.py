import time
import requests
from globals import verbose_output


def wait_until_200(execution_parameters):
    time.sleep(0.05)
    start_time = time.time()
    result = True
    end = 0.0
    full_url = ""
    while True:
        try:
            full_url, response = execution_parameters["url_callback"]()
            if response.status_code == 200:
                end = time.perf_counter()
                if execution_parameters["verbose"]:
                    verbose_output.append("Response from the call to {url} = {content}"
                                          .format(content=response.content, url=full_url))
                break
        except requests.exceptions.ConnectionError:
            pass
        time.sleep(0.05)  # 50 ms wait
        if time.time() - start_time >= 10:
            print("Timeout reached for endpoint {url}".format(url=full_url))
            result = False
            break
    return result, end
