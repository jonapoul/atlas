plugins {
  id("modular.convention.plugin")
}

gradlePlugin {
  plugins {
    create("modular-graphviz") {
      id = "dev.jonpoulton.modular.graphviz"
      implementationClass = "modular.graphviz.GraphvizModularPlugin"
      displayName = "Modular Graphviz"
      tags.addAll("graphviz", "dotfile", "dot", "svg", "png")
    }
  }
}

dependencies {
  api(project(":modular-core"))
}
