plugins {
  id("atlas.convention.plugin")
}

gradlePlugin {
  plugins {
    create("atlas-d2") {
      id = "dev.jonpoulton.atlas.d2"
      implementationClass = "atlas.d2.D2AtlasPlugin"
      displayName = "Atlas D2"
      tags.addAll("d2", "dagre", "elk", "lala")
    }
  }
}

dependencies {
  api(project(":atlas-core"))
}
