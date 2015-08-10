/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.model.*
import org.mikeneck.gradle.model.Hello

class HelloTestKit extends RuleSource {

    static final String TASK_NAME = 'helloTestKit'

    @Model
    static void hello(Hello hello) {}

    @Defaults
    static void defaultRepeat(Hello hello) {
        hello.dirName = 'hello'
        hello.fileName = 'hello.txt'
        hello.contents.each {
            it.repeat = 1
        }
    }

    @Validate
    static void validateModel(Hello hello) {
        if (hello.dirName == null || hello.dirName.isEmpty()) {
            throw new IllegalConfigurationException('dirName should be not null and not empty.')
        }
        if (hello.fileName == null) {
            throw new IllegalConfigurationException('fileName should be not null and not empty.')
        }
        hello.contents.each {
            if(it.repeat < 1) {
                throw new IllegalConfigurationException('repeat should be larger than or equals to 1.')
            }
        }
    }

    @Mutate
    void createTask(ModelMap<Task> tasks, Hello hello) {
        tasks.create(TASK_NAME) {
            Project pj = project
            group = 'Hello Test Kit'
            description = 'Sample task for Gradle Test Kit'
            def dir = pj.file("${pj.buildDir}/${hello.dirName}")
            def file = pj.file("${dir}/${hello.fileName}")
            def w = new StringWriter()
            hello.contents.each {c ->
                c.repeat.times {
                    w << "${c.line}\n"
                }
            }
            doLast {
                file.write(w.toString(), 'UTF-8')
            }
        }
    }
}
