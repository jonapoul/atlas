import modular.graphviz.spec.ArrowType.None
import modular.graphviz.spec.ArrowType.Normal
import modular.graphviz.spec.FileFormat.Svg
import modular.graphviz.spec.LayoutEngine.Dot
import modular.graphviz.spec.LinkStyle.Bold
import modular.graphviz.spec.LinkStyle.Dotted
import modular.graphviz.spec.LinkStyle.Solid
import modular.graphviz.spec.RankDir.TopToBottom
import modular.mermaid.spec.ConsiderModelOrder.PreferEdges
import modular.mermaid.spec.CycleBreakingStrategy.Interactive
import modular.mermaid.spec.Look.HandDrawn
import modular.mermaid.spec.NodePlacementStrategy.LinearSegments
import modular.mermaid.spec.Theme.Forest

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
  ignoredConfigs = setOf("debug", "kover", "ksp", "test")
  ignoredModules = emptySet()
  printFilesToConsole = false

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

  outputs {
    chartRootFilename = "charts"
    legendRootFilename = "legend"
    saveChartsRelativeToSubmodule("charts")
    saveLegendsRelativeToRootModule("legend")
  }

  graphViz {
    adjustSvgViewBox = true
    dpi = 100
    fontSize = 30
    rankSep = 1.5f
    writeReadme = true
    checkOutputs = true

    arrowHead(Normal)
    arrowTail(None)
    fileFormat(Svg)
    layoutEngine(Dot)
    rankDir(TopToBottom)
  }

  mermaid {
    animateLinks = false
    writeReadme = false

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
