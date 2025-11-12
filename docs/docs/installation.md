---
title: Installation
description: Steps for adding Atlas to your Gradle project
icon: lucide/package-open
---

# Installation

For proper release builds, you want to add the central repository to your `settings.gradle.kts` file:

``` kotlin
pluginManagement {
  repositories {
    mavenCentral()
  }
}
```

Or for pre-release snapshots builds (the latest state of the main branch in this repo), add the Maven Central snapshots repo:

``` kotlin
pluginManagement {
  repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/")
  }
}
```

Then in your ***root*** `build.gradle.kts` file:

``` kotlin
plugins {
  // 🚨🚨🚨 WARNING: You can only apply one of these at a time! 🚨🚨🚨
  id("dev.jonpoulton.atlas.d2") version "x.y.z"
  id("dev.jonpoulton.atlas.graphviz") version "x.y.z"
  id("dev.jonpoulton.atlas.mermaid") version "x.y.z"
}
```

Then generate your diagrams by running:

``` shell
gradle atlasGenerate
```

or validate them by running:

``` shell
gradle atlasCheck
```

That's all you need to get it working! See the next pages for further configuration of each of the above plugins.
