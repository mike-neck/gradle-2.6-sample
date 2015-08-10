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

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class HelloTestKitSpec2 extends Specification {

    @Rule
    final TemporaryFolder projectDir = new TemporaryFolder()

    final ClassLoader loader = getClass().classLoader

    File buildFile

    def setup() {
        buildFile = projectDir.newFile('build.gradle')
    }

    def 'plugin[HelloTestKit]/task[model] shows hello model information'() {
        given:
        buildFile << loader.getResource('test-scripts/test2.gradle').text

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withArguments('model')
                .build()

        then:
        result.standardOutput.contains('+ hello')
        println result.standardOutput
    }

    def 'plugin[HelloTestKit]/task[helloTestKit] creates file build/test/test.txt'() {
        given:
        buildFile << loader.getResource('test-scripts/test2.gradle').text

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withArguments('helloTestKit')
                .build()

        then:
        result.task(':helloTestKit').outcome == TaskOutcome.SUCCESS
        def buildDir = new File(projectDir.root, 'build')
        def testDir = new File(buildDir, 'test')
        def outputFile = new File(testDir, 'test.txt')
        outputFile.text.contains($/Hello Gradle Test Kit
Hello Gradle Test Kit
Hello Gradle Test Kit
/$)
    }
}
