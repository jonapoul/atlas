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
  moduleTypes {
    builtIns()
  }

  moduleNames {
    replace("^:modules:", ":")
  }

  dotFile {
    legend {
      file = file("modules-legend.dot")
      cellBorder = 1
      cellPadding = 4
      cellSpacing = 0
      tableBorder = 0
    }

    chart {
      file = file("modules.dot")
      showArrows = true
      rankSep = 1.5f
      rankDir = RankDir.TopToBottom
      dpi = 100
    }
  }
}
