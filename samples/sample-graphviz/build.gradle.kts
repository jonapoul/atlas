import modular.graphviz.ArrowType.None
import modular.graphviz.ArrowType.Normal
import modular.graphviz.FileFormat.Svg
import modular.graphviz.LayoutEngine.Dot
import modular.graphviz.LinkStyle.Bold
import modular.graphviz.LinkStyle.Dotted
import modular.graphviz.LinkStyle.Solid
import modular.graphviz.RankDir.TopToBottom

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  id("dev.jonpoulton.modular.trunk")
}

modular {
  alsoTraverseUpwards = true
  generateOnSync = true

  moduleTypes {
    builtIns()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(Solid)
    implementation(Dotted)
  }

  outputs {
    saveChartsRelativeToSubmodule("charts")
    saveLegendsRelativeToRootModule("legend")
  }

  graphViz {
    adjustSvgViewBox = true
    dpi = 100
    fontSize = 30
    rankSep = 1.5f
    checkOutputs = true

    arrowHead(Normal)
    arrowTail(None)
    fileFormat(Svg)
    layoutEngine(Dot)
    rankDir(TopToBottom)
  }
}
