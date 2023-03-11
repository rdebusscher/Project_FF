import atexit
import os
import signal
import time
from subprocess import Popen, PIPE, TimeoutExpired
import concurrent.futures
from endpoint import wait_until_200
from globals import verbose_output

# Keep track of all processes started and clean them up
processes = []
runtime_pids = []
cleanup_registered = False


def cleanup_processes():
    # Registered by AtbashPerformanceRunner constructor
    print("Cleaning processes up before exit...")
    for pid in runtime_pids:
        os.kill(pid, signal.SIGKILL)
    for p in processes:
        if p.poll() is None:
            p.terminate()
    global cleanup_registered  # cleanup_registered is defined outside function
    cleanup_registered = False


class AtbashPerformanceRunner:

    def __init__(self):
        self.process = None
        self.start_time = 0.0
        self.end_time = 0.0
        self.runtime_pid = None

        global cleanup_registered  # cleanup_registered is not defined within the class
        if not cleanup_registered:
            cleanup_registered = True
            atexit.register(cleanup_processes)

    def launch_runtime_java(self, jar_file, jvm_params=None, user_params=None):
        params = ["java"]
        if jvm_params is not None:
            params.extend(jvm_params)
        params.extend(["-jar", jar_file])
        if user_params is not None:
            params.extend(user_params)

        self.start_time = time.perf_counter()
        # Run a command
        process = Popen(params, stdout=PIPE, stderr=PIPE)
        processes.append(process)
        self.runtime_pid = process.pid
        self.process = process

    def launch_runtime_shell(self, shell_script):
        self.start_time = time.perf_counter()
        # Run a command
        process = Popen(shell_script, stdout=PIPE, stderr=PIPE)
        processes.append(process)

        self.process = process
        self.runtime_pid = self.__get_runtime_pid()
        runtime_pids.append(self.runtime_pid)

    def __get_runtime_pid(self):

        filename = 'RUNTIME.pid'
        max_wait_time = 5  # seconds

        start_time = time.time()
        while not os.path.exists(filename):
            if time.time() - start_time >= max_wait_time:
                print(f"File '{filename}' not found within {max_wait_time} seconds.")
                break
            time.sleep(0.1)  # wait for 0.1 seconds before checking again

        if os.path.exists(filename):
            with open(filename, 'r') as f:
                file_contents = f.read()
        return int(file_contents.strip())

    def __execute_command(self, command_params, time_out=10):
        # Run a command
        process = Popen(command_params, stdout=PIPE, stderr=PIPE)
        processes.append(process)

        # Wait for the process to terminate
        try:
            out, err = process.communicate(timeout=time_out)

        except TimeoutExpired:
            process.kill()
            out, err = process.communicate()

        return out.decode() + err.decode()

    def determine_memory_usage(self, execution_parameters):
        #jcmd_output = self.__execute_command(["jcmd", str(self.runtime_pid), "VM.native_memory", "summary"])
        ps_output = self.__execute_command(["ps", "-o", "rss", "-p", str(self.runtime_pid)])
        rss_mem = ps_output.split("\n")[1]
        #java_mem = jcmd_output.split("\n")[6].split("=")[-1]
        #print("RSS = %s, Java = %s " % (rss_mem, java_mem))
        print("RSS = {mem}".format(mem=rss_mem))
        #if execution_parameters["verbose"]:
        #    verbose_output.append("Memory usage details")
        #    verbose_output.append(jcmd_output)

    def wait_for(self, execution_parameters):
        if self.runtime_pid == -1:
            return False
        with concurrent.futures.ThreadPoolExecutor() as executor:
            future = executor.submit(wait_until_200, execution_parameters)
            (healthy, self.end_time) = future.result()
        return healthy

    def calculate_first_response_delay(self):
        duration_ms = (self.end_time - self.start_time) * 1000
        print("Duration: %.2f ms" % duration_ms)

    def end_process(self):
        if self.process.pid != self.runtime_pid:
            # Started through shell, so we have to kill Runtime
            os.kill(self.runtime_pid, signal.SIGKILL)
            runtime_pids.remove(self.runtime_pid)
        self.process.terminate()
        out, err = self.process.communicate()

        return out.decode() + err.decode()
