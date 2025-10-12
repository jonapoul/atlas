@file:Suppress("ktlint:standard:no-wildcard-imports")

import atlas.d2.ArrowType
import atlas.d2.Direction
import atlas.d2.FileFormat
import atlas.d2.FillPattern
import atlas.d2.Font
import atlas.d2.LayoutEngine
import atlas.d2.LinkStyle
import atlas.d2.Location
import atlas.d2.Position
import atlas.d2.Shape
import atlas.d2.TextTransform
import atlas.d2.Theme

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("dev.jonpoulton.atlas.d2")
}

atlas {
  alsoTraverseUpwards = false
  displayLinkLabels = true
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
      fontColor = "orange"
      strokeDash = 3
    }
    api(style = LinkStyle.Basic, color = "greenyellow") {
      strokeWidth = 5
    }
    implementation(style = LinkStyle.Dotted, color = "fuchsia") {
      textTransform = TextTransform.Uppercase
    }
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
      put("(** -> **)[*].style.font-color", "black")
    }
  }
}
