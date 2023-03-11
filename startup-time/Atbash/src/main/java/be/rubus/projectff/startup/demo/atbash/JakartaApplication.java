/*
 * Copyright 2023 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.rubus.projectff.startup.demo.atbash;

import be.atbash.runtime.jakarta.executable.JakartaSERunnerBuilder;
import be.rubus.projectff.startup.demo.atbash.json.PersonController;

public class JakartaApplication {

    public static void main(String[] args) {

        JakartaSERunnerBuilder.newBuilder(HelloController.class, PersonController.class)
                .run();

    }
}
