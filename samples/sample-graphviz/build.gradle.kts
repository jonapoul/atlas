import atlas.graphviz.ArrowType.Crow
import atlas.graphviz.ArrowType.Ediamond
import atlas.graphviz.ArrowType.None
import atlas.graphviz.Dir.Both
import atlas.graphviz.FileFormat.Svg
import atlas.graphviz.LayoutEngine.Dot
import atlas.graphviz.LinkStyle.Bold
import atlas.graphviz.LinkStyle.Dotted
import atlas.graphviz.LinkStyle.Solid
import atlas.graphviz.NodeStyle.Filled
import atlas.graphviz.NodeStyle.Radial
import atlas.graphviz.RankDir.TopToBottom
import atlas.graphviz.Shape.Box
import atlas.graphviz.Shape.Rarrow

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.kmp) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.atlas.graphviz")
}

atlas {
  alsoTraverseUpwards = true
  displayLinkLabels = true
  generateOnSync = true
  checkOutputs = true

  projectTypes {
    androidApp {
      shape = Rarrow
      style = Radial
    }
    kotlinMultiplatform { fontColor = "red" }
    androidLibrary {
      color = "crimson:cyan4"
      gradientAngle = 90
    }
    kotlinJvm()
    java { color = null }
    other { color = "#444444" }
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange") { arrowHead = Crow }
    api(Solid) { weight = 5 }
    implementation(Dotted) {
      dir = Both
      arrowTail = Ediamond
    }
  }

  graphviz {
    fileFormat = Svg
    layoutEngine = Dot

    node {
      fontName = "Courier New"
      peripheries = 3
      style = Filled
      shape = Box
      lineColor = "#4C0000"
      fontColor = "white"
    }

    graph {
      bgColor = "MidnightBlue"
      fontSize = "30"
      rankDir = TopToBottom
      rankSep = 1.5
    }

    edge {
      arrowHead = Ediamond
      arrowTail = None
      fontColor = "white"
      labelFloat = false
      linkColor = "red"
    }
  }
}
