@file:Suppress("ktlint:standard:no-wildcard-imports")

import modular.d2.ArrowType
import modular.d2.Direction
import modular.d2.FileFormat
import modular.d2.FillPattern
import modular.d2.Font
import modular.d2.LayoutEngine
import modular.d2.LinkStyle
import modular.d2.Location
import modular.d2.Position
import modular.d2.Shape
import modular.d2.TextTransform
import modular.d2.Theme

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
    androidApp {
      shape = Shape.Hexagon
      strokeWidth = 10
      stroke = "black"
      fontColor = "black"
    }
    kotlinMultiplatform()
    androidLibrary {
      fontColor = "red"
      multiple = true
      italic = true
    }
    kotlinJvm { fillPattern = FillPattern.Lines }
    java { animated = true }
    other()
  }

  linkTypes {
    "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange") {
      opacity = 0.5f
      strokeDash = 3
    }
    api(LinkStyle.Basic, "greenyellow") {
      textTransform = TextTransform.Uppercase
      strokeWidth = 3
    }
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
