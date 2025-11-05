---
title: Atlas
description: Overview of the Atlas Gradle Plugin's functionality and basic setup
---

# Atlas Gradle Plugin

Atlas is a Gradle plugin for generating, configuring and curating diagrams to illustrate your project's project structure:

   1. Fully supports Gradle 9 and its configuration caching.
   2. Supports three separate frameworks as outputs:
      - **D2**: [Official docs here](https://d2lang.org/)
      - **Graphviz**: [Official docs here](https://graphviz.org/)
      - **Mermaid**: [Official docs here](https://mermaid.js.org/)
   3. Offers wide-ranging APIs for customizing your charts
   4. Supports `gradle check`-ing your generated diagrams, to validate that they match the current state of your project
   5. Supports attaching the chart generation to IntelliJ's sync button, so you don't even need to run it manually.

!!! info "Inspiration"

    This project was built as a spiritial successor to [com.vanniktech.dependency.graph.generator](https://github.com/vanniktech/gradle-dependency-graph-generator-plugin) project - but with more configurability and targeting modern Gradle releases.

# Installation

For proper release builds, you want to add the central repository to your `settings.gradle.kts` file:

```kotlin
pluginManagement {
  repositories {
    mavenCentral()
  }
}
```

Or for pre-release snapshots builds (the latest state of the main branch in this repo), add the Maven Central snapshots repo:

```kotlin
pluginManagement {
  repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/")
  }
}
```

Then in your ***root*** `build.gradle.kts` file:

```kotlin
plugins {
  // 🚨🚨🚨 WARNING: You can only apply one of these at a time! 🚨🚨🚨
  id("dev.jonpoulton.atlas.d2") version "x.y.z"
  id("dev.jonpoulton.atlas.graphviz") version "x.y.z"
  id("dev.jonpoulton.atlas.mermaid") version "x.y.z"
}
```

Then generate your diagrams by running:

```shell
gradle atlasGenerate
```

or validate them by running:

```shell
gradle atlasCheck
```

That's all you need to get it working! See the next pages for further configuration of each of the above plugins.
