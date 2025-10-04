import modular.core.LinkStyle.Bold
import modular.core.LinkStyle.Dotted
import modular.core.LinkStyle.Solid
import modular.d2.ArrowType
import modular.d2.Direction
import modular.d2.FileFormat
import modular.d2.LayoutEngine
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

  moduleTypes {
    builtIns()
  }

  linkTypes {
    "jvmMainImplementation"(style = Bold, color = "orange")
    api(style = Solid, color = "greenyellow")
    implementation(style = Dotted, color = "fuchsia")
  }

  pathTransforms {
    remove(pattern = "^:sample-")
    replace(pattern = "-", replacement = " ")
  }

  d2 {
    arrowType = ArrowType.Box
    direction = Direction.Right
    fileFormat = FileFormat.Svg
    layoutEngine = LayoutEngine.Elk
    sketch = true
    theme = Theme.ShirleyTemple
    themeDark = Theme.DarkMauve
    style {
      stroke = "floralwhite"
      strokeWidth = 3
      strokeDash = 4
      doubleBorder = true
    }
  }
}
