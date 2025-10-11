@file:Suppress("ktlint:standard:no-wildcard-imports")

import modular.core.*
import modular.d2.*

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.modular.d2")
}

modular {
  alsoTraverseUpwards = false
  generateOnSync = true
  groupModules = true

  moduleTypes {
    useDefaults()
  }

  linkTypes {
    "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
    api(style = LinkStyle.Basic, color = "greenyellow")
    implementation(style = LinkStyle.Dotted, color = "fuchsia")
  }

  d2 {
    animateLinks = true
    center = true
    direction = Direction.Right
    fileFormat = FileFormat.Svg
    groupLabelLocation = Location.Border
    groupLabelPosition = Position.BottomCenter
    layoutEngine = LayoutEngine.Elk
    pad = 100
    sketch = true
    theme = Theme.ShirleyTemple
    themeDark = Theme.DarkMauve

    rootStyle {
      stroke = "floralwhite"
      strokeWidth = 3
      strokeDash = 4
      doubleBorder = true
    }

    globalProps {
      arrowType = ArrowType.Box
      fillArrowHeads = true
      font = Font.Mono
      fontSize = 32
    }
  }
}
