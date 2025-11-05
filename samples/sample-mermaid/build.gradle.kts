import atlas.mermaid.ConsiderModelOrder.PreferEdges
import atlas.mermaid.CycleBreakingStrategy.Interactive
import atlas.mermaid.LinkStyle.Basic
import atlas.mermaid.LinkStyle.Bold
import atlas.mermaid.LinkStyle.Dashed
import atlas.mermaid.Look.HandDrawn
import atlas.mermaid.NodePlacementStrategy.LinearSegments
import atlas.mermaid.Theme.Forest

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.kmp) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.atlas.mermaid")
}

atlas {
  alsoTraverseUpwards = false
  displayLinkLabels = true
  generateOnSync = true

  projectTypes {
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
