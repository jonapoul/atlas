plugins {
  id("atlas.convention.plugin")
}

gradlePlugin {
  plugins {
    create("atlas-graphviz") {
      id = "dev.jonpoulton.atlas.graphviz"
      implementationClass = "atlas.graphviz.GraphvizAtlasPlugin"
      displayName = "Atlas Graphviz"
      tags.addAll("graphviz", "dotfile", "dot", "svg", "png")
    }
  }
}

dependencies {
  api(project(":atlas-core"))
}
