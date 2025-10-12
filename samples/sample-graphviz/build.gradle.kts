import modular.graphviz.ArrowType.Ediamond
import modular.graphviz.ArrowType.None
import modular.graphviz.FileFormat.Svg
import modular.graphviz.LayoutEngine.Dot
import modular.graphviz.LinkStyle.Bold
import modular.graphviz.LinkStyle.Dotted
import modular.graphviz.LinkStyle.Solid
import modular.graphviz.NodeStyle.Filled
import modular.graphviz.NodeStyle.Radial
import modular.graphviz.RankDir.TopToBottom
import modular.graphviz.Shape.Box
import modular.graphviz.Shape.Rarrow

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.modular.graphviz")
}

modular {
  alsoTraverseUpwards = true
  generateOnSync = true
  checkOutputs = true

  moduleTypes {
    androidApp {
      shape = Rarrow
      style = Radial
    }
    kotlinMultiplatform { fontColor = "white" }
    androidLibrary {
      color = "crimson:cyan4"
      gradientAngle = 90
    }
    kotlinJvm()
    java { color = null }
    other()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(Solid)
    implementation(Dotted)
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
      linkColor = "red"
      labelFloat = true
      fontColor = "lime"
    }
  }
}
