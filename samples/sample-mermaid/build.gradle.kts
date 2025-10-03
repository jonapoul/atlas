import modular.core.spec.LinkStyle.Bold
import modular.core.spec.LinkStyle.Dotted
import modular.core.spec.LinkStyle.Solid
import modular.mermaid.ConsiderModelOrder.PreferEdges
import modular.mermaid.CycleBreakingStrategy.Interactive
import modular.mermaid.Look.HandDrawn
import modular.mermaid.NodePlacementStrategy.LinearSegments
import modular.mermaid.Theme.Forest

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.modular.mermaid")
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

  pathTransforms {
    remove(pattern = "^:sample-")
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

    themeVariables {
      background("#FFF")
      fontFamily("arial")
      lineColor("#55FF55")
      primaryBorderColor("#FF5555")
      primaryColor("#ABC123")
      darkMode(true)
      fontSize("30px")

      put("defaultLinkColor", "#5555FF")
    }
  }
}
