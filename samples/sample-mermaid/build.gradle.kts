import modular.mermaid.ConsiderModelOrder.PreferEdges
import modular.mermaid.CycleBreakingStrategy.Interactive
import modular.mermaid.LinkStyle.Basic
import modular.mermaid.LinkStyle.Bold
import modular.mermaid.LinkStyle.Dashed
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
  displayLinkLabels = true
  generateOnSync = true

  moduleTypes {
    androidApp()

    kotlinMultiplatform {
      fontColor = "white"
      strokeDashArray = "4 3 2 1"
      fontSize = "20px"
    }

    androidLibrary()
    kotlinJvm()
    java()
    other()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(Basic) { strokeWidth = "5px" }
    implementation(Dashed) { stroke = "aqua" }
  }

  pathTransforms {
    remove(pattern = "^:sample-")
    replace(pattern = "-", replacement = " ")
  }

  mermaid {
    animateLinks = false
    look = HandDrawn
    theme = Forest

    elk {
      mergeEdges = true
      forceNodeModelOrder = true
      nodePlacementStrategy = LinearSegments
      cycleBreakingStrategy = Interactive
      considerModelOrder = PreferEdges
    }

    themeVariables {
      background = "#FFF"
      fontFamily = "arial"
      lineColor = "#55FF55"
      primaryBorderColor = "#FF5555"
      primaryColor = "#ABC123"
      darkMode = true
      fontSize = "30px"

      put("defaultLinkColor", "#5555FF")
    }
  }
}
