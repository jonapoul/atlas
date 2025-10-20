---
title: Common
description: Common configuration steps across all Atlas Gradle plugins
---

# Configuration

## Overview

Configuration is primarily done via the `atlas` Gradle extension function, accessible in your root build
file. [See here for the KDoc](https://jonapoul.github.io/atlas/api/atlas-core/atlas.core/-atlas-extension),
or [here for the source file](https://github.com/jonapoul/atlas/blob/main/atlas-core/src/main/kotlin/atlas/core/AtlasExtension.kt).

```kotlin
// none of these are required - these values are the defaults
atlas {
  alsoTraverseUpwards = false
  checkOutputs = true
  displayLinkLabels = false
  generateOnSync = false
  groupModules = false
  ignoredConfigs = setOf("debug", "kover", "ksp", "test")
  ignoredModules = emptySet<String>()
  printFilesToConsole = false

  pathTransforms {
    // ...
  }

  moduleTypes {
    // ...
  }

  linkTypes {
    // ...
  }
}
```

Alternatively, if calling from a `buildSrc` Kotlin file (or similar), access with:

```kotlin
project.extensions.configure<AtlasExtension> {
  // ...
}
```

Any other configs beyond these are specific to the particular plugin you applied, see:

- [Graphviz](usage-graphviz.md)
- [D2](usage-d2.md)
- [Mermaid](usage-mermaid.md)

## TODO

TODO: Draw the rest of the owl
