import modular.core.spec.LinkStyle.Bold
import modular.core.spec.LinkStyle.Dotted
import modular.core.spec.LinkStyle.Solid
import modular.graphviz.ArrowType.None
import modular.graphviz.ArrowType.Normal
import modular.graphviz.FileFormat.Svg
import modular.graphviz.LayoutEngine.Dot
import modular.graphviz.RankDir.TopToBottom

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
    builtIns()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(Solid)
    implementation(Dotted)
  }

  graphviz {
    adjustSvgViewBox = true
    backgroundColor = "transparent"
    dpi = 100
    fontSize = 30
    rankSep = 1.5f

    arrowHead(Normal)
    arrowTail(None)
    fileFormat(Svg)
    layoutEngine(Dot)
    rankDir(TopToBottom)
  }
}
