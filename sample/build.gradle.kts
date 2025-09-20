import modular.graphviz.spec.ArrowType
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.LinkStyle
import modular.graphviz.spec.RankDir
import modular.mermaid.spec.ConsiderModelOrder
import modular.mermaid.spec.CycleBreakingStrategy
import modular.mermaid.spec.Look
import modular.mermaid.spec.NodePlacementStrategy
import modular.mermaid.spec.Theme

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
    alsoTraverseUpwards = false
  }

  moduleTypes {
    builtIns()
  }

  linkTypes {
    "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
    api(LinkStyle.Solid)
    implementation(LinkStyle.Dotted)
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

    arrowHead(ArrowType.Normal)
    arrowTail(ArrowType.None)
    layoutEngine(LayoutEngine.Dot)
    rankDir(RankDir.TopToBottom)
    dpi = 100
    fontSize = 30
    rankSep = 1.5f

    fileFormats {
      eps()
      png()
      svg()
    }
  }

  mermaid {
    fileExtension = "mmd"

    animateLinks = false
    look(Look.HandDrawn)
    theme(Theme.Forest)

    elk {
      mergeEdges(true)
      forceNodeModelOrder(true)
      nodePlacementStrategy(NodePlacementStrategy.LinearSegments)
      cycleBreakingStrategy(CycleBreakingStrategy.Interactive)
      considerModelOrder(ConsiderModelOrder.PreferEdges)
    }
  }
}
