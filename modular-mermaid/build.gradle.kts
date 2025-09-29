plugins {
  id("modular.convention.plugin")
}

gradlePlugin {
  plugins {
    create("modular-mermaid") {
      id = "dev.jonpoulton.modular.mermaid"
      implementationClass = "modular.mermaid.MermaidModularPlugin"
      displayName = "Modular Mermaid"
      tags.addAll("mermaid", "markdown", "mmd", "elk")
    }
  }
}

dependencies {
  api(project(":modular-core"))
  testImplementation(project(":modular-test"))
}
