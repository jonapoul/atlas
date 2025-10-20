plugins {
  id("atlas.convention.plugin")
}

gradlePlugin {
  plugins {
    create("atlas-mermaid") {
      id = "dev.jonpoulton.atlas.mermaid"
      implementationClass = "atlas.mermaid.MermaidAtlasPlugin"
      displayName = "Atlas Mermaid"
      tags.addAll("mermaid", "markdown", "mmd", "elk")
    }
  }
}

dependencies {
  api(project(":atlas-core"))
}
