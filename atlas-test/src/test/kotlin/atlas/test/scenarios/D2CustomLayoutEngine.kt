package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.KOTLIN_VERSION

internal object D2CustomLayoutEngine : D2Scenario by D2Basic {
  override val rootBuildFile = """
    import atlas.d2.FileFormat
    import atlas.d2.LayoutEngine

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes.useDefaults()

      d2 {
        layoutEngine.elk()
        fileFormat = FileFormat.Svg
      }
    }
  """.trimIndent()
}
