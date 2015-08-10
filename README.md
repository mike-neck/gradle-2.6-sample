Gradle 2.6 Feature sample
===

This is a sample project trying following [Gradle 2.6 Features](https://docs.gradle.org/2.6/release-notes)

* [Support for functionally testing Gradle plugins](https://docs.gradle.org/2.6/release-notes#support-for-functionally-testing-gradle-plugins)
* [Rule based model configuration reporting improvements](https://docs.gradle.org/2.6/release-notes#rule-based-model-configuration-reporting-improvements)

Support for functionally testing Gradle plugins
===

This is interesting feature. This feature enables you to try sample scripts and test it.

Please see the source [HelloTestKitSpec2#task-helloTestKit](https://github.com/mike-neck/gradle-2.6-sample/blob/master/src/test/groovy/org/mikeneck/gradle/HelloTestKitSpec2.groovy#L53)

Rule based model configuration reporting improvements
===

This new feature add more information to model dsl.

Additional information is...

* model type
* model creator
* rules apply to

Please see the source [HelloTestKitSpec2#task-model](https://github.com/mike-neck/gradle-2.6-sample/blob/master/src/test/groovy/org/mikeneck/gradle/HelloTestKitSpec2.groovy#L38)
