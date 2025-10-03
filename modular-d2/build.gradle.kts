plugins {
  id("modular.convention.plugin")
}

gradlePlugin {
  plugins {
    create("modular-d2") {
      id = "dev.jonpoulton.modular.d2"
      implementationClass = "modular.d2.D2ModularPlugin"
      displayName = "Modular D2"
      tags.addAll("d2", "dagre", "elk", "lala")
    }
  }
}

dependencies {
  api(project(":modular-core"))
}
