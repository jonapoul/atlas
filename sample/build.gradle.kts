import modular.graphviz.spec.ArrowType
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.LinkStyle
import modular.graphviz.spec.RankDir

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  id("dev.jonpoulton.modular.trunk")
}

modular {
  general {
    adjustSvgViewBox = true
    generateOnSync = true
    ignoredConfigs = setOf("debug", "kover", "ksp", "test")
    ignoredModules = emptySet()
    supportUpwardsTraversal = false
  }

  moduleTypes {
    builtIns()
  }

  modulePathTransforms {
    remove(pattern = "^:sample-")
    replace(pattern = "lib", replacement = "Lib")
    replace(pattern = "-", replacement = " ")
  }

  outputs {
    chartRootFilename = "modules"
    legendRootFilename = "legend"
    saveChartsRelativeToSubmodule("modules")
    saveLegendsRelativeToRootModule("legend")
  }

  graphViz {
    fileExtension = "dot"

    legend {
      cellBorder = 1
      cellPadding = 4
      cellSpacing = 0
      tableBorder = 0
    }

    chart {
      arrowHead(ArrowType.Normal)
      arrowTail(ArrowType.None)
      layoutEngine(LayoutEngine.Dot)
      rankDir(RankDir.TopToBottom)
      dpi = 100
      fontSize = 30
      rankSep = 1.5f
    }

    linkTypes {
      "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
      api(LinkStyle.Solid)
      implementation(LinkStyle.Dotted)
    }

    fileFormats {
      eps()
      png()
      svg()
    }
  }
}
