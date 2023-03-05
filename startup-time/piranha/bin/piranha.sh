#!/bin/bash
#
# Copyright 2023 Rudy De Busscher (https://www.atbash.be)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


launch_dir=$(pwd)
cd "$(dirname "$0")"

java -XX:NativeMemoryTracking=detail -jar piranha-dist-coreprofile-23.2.0.jar --context-path ROOT --war-file ../target/piranha.war & echo $! > "$launch_dir/RUNTIME.pid"