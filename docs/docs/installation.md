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
  id("dev.jonpoulton.atlas") version "x.y.z"
}
```

Nothing is generated until you pick a framework, which you do by configuring its block. Use as many as you like:

``` kotlin
atlas {
  d2()          // writes to atlas/d2/
  graphviz()    // writes to atlas/graphviz/
  mermaid()     // writes to atlas/mermaid/
}
```

Each framework writes into its own directory, so enabling several at once never has two of them fighting over the same file. Passing a configuration block switches the framework on too, so this is enough:

``` kotlin
atlas {
  mermaid {
    theme = Theme.Forest
  }
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
