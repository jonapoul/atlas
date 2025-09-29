import modular.graphviz.LinkStyle.Bold
import modular.graphviz.LinkStyle.Dotted
import modular.graphviz.LinkStyle.Solid
import modular.mermaid.ConsiderModelOrder.PreferEdges
import modular.mermaid.CycleBreakingStrategy.Interactive
import modular.mermaid.Look.HandDrawn
import modular.mermaid.NodePlacementStrategy.LinearSegments
import modular.mermaid.Theme.Forest

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  id("dev.jonpoulton.modular.trunk")
}

modular {
  alsoTraverseUpwards = false
  generateOnSync = true

  moduleTypes {
    builtIns()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(Solid)
    implementation(Dotted)
  }

  modulePathTransforms {
    remove(pattern = "^:sample-")
    replace(pattern = "lib", replacement = "Lib")
    replace(pattern = "-", replacement = " ")
  }

  mermaid {
    animateLinks = false

    look(HandDrawn)
    theme(Forest)

    elk {
      mergeEdges(true)
      forceNodeModelOrder(true)
      nodePlacementStrategy(LinearSegments)
      cycleBreakingStrategy(Interactive)
      considerModelOrder(PreferEdges)
    }
  }
}
