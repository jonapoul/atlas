import modular.spec.RankDir

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  id("dev.jonpoulton.modular.trunk")
}

modular {
  generateOnSync = true
  ignoredConfigs = setOf("debug", "kover", "ksp", "test")
  ignoredModules = emptySet()
  supportUpwardsTraversal = false

  moduleTypes {
    builtIns()
  }

  moduleNames {
    remove(pattern = "^:sample-")
    replace(pattern = "lib", replacement = "abc")
    replace(pattern = "-", replacement = "_")
  }

  outputs {
    chartRootFilename = "modules"
    legendRootFilename = "legend"
    saveChartsRelativeToSubmodule("modules")
    saveLegendsRelativeToRootModule("legend")
  }

  dotFile {
    extension = "dot"

    legend {
      cellBorder = 1
      cellPadding = 4
      cellSpacing = 0
      tableBorder = 0
    }

    chart {
      showArrows = true
      rankSep = 1.5f
      rankDir = RankDir.TopToBottom
      dpi = 100
    }

    fileFormats {
      eps()
      png()
      svg()
    }

    experimental {
      adjustSvgViewBox = true
    }
  }
}
