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
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class HelloTestKitSpec1 extends Specification {

    @Rule
    final TemporaryFolder projectDir = new TemporaryFolder()

    final ClassLoader loader = getClass().classLoader

    File buildFile

    String script

    def setup() {
        buildFile = projectDir.newFile('build.gradle')
        script = $/
${loadScript()}
${loader.getResource('test-scripts/test1.gradle').text}
/$
        println script
    }

    String loadScript() {
        def pluginClassUrl = HelloTestKit.class.protectionDomain.codeSource.location
        def path = Paths.get(pluginClassUrl.toURI())
        if (pluginClassUrl.protocol == 'file' && "${pluginClassUrl}".endsWith('.jar')) {
            return """buildscript {
    dependencies {
        classpath file('${path}')
    }
}
"""
        } else {
            def granpa = path.parent.parent
            def runOnIdea = granpa.endsWith('out')
            def resPath = runOnIdea ?
                    path :
                    granpa.resolve('resources/main')
            def urls = ([path, resPath] as Set).collect {
                it.toUri().toURL()
            }.collect {
                "new URL('${it}')"
            }.join(', ')
            return """[$urls].each {
    project.class.classLoader.addURL(it)
}
assert project.class.classLoader.getResource('META-INF/gradle-plugins/hello.properties') != null
"""
        }
    }

    def 'helloTestKit creates build/test/test.txt'() {
        given:
        buildFile << script

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withArguments('model')
                .build()

        then:
        result.standardOutput.contains('hello')
//        result.task(HelloTestKit.TASK_NAME).outcome == TaskOutcome.SUCCESS
//        projectDir.newFile('build/test/test.txt').text.contains($/Hello Gradle Test Kit
//Hello Gradle Test Kit
//Hello Gradle Test Kit
///$)
    }
}
