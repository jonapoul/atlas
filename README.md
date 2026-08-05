# Atlas Gradle Plugin

### [jonapoul.github.io/atlas-gradle-plugin](https://jonapoul.github.io/atlas-gradle-plugin)

A Gradle settings plugin for generating diagrams of your project's module structure, rendered with
[D2](https://d2lang.org/), [Graphviz](https://graphviz.org/) or [Mermaid](https://mermaid.js.org/) -
any combination of the three. Supports Gradle 9, the configuration cache and isolated projects.

## Quick start

Atlas is applied and configured in `settings.gradle.kts`, not in a build script:

```kotlin
pluginManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("dev.jonpoulton.atlas") version "x.y.z"
}

include(":app", ":core")

atlas {
  projectTypes { useDefaults() }
  mermaid()
}
```

Nothing is generated until you configure at least one framework block. Your subprojects need no
changes - the settings plugin wires up every project in the build.

Then:

```shell
gradle atlasGenerate   # write the diagrams
gradle atlasCheck      # verify they match the current project structure
```

See the [documentation](https://jonapoul.github.io/atlas-gradle-plugin) for the full configuration
reference, and [`samples/`](samples) for complete worked examples of each framework.

## License

```
Copyright (C) 2025 Jon Poulton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
