/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.D2Scenario
import modular.test.KOTLIN_VERSION

internal object D2CustomLayoutEngine : D2Scenario by D2Basic {
  override val rootBuildFile = """
    import modular.d2.FileFormat
    import modular.d2.LayoutEngine

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes.useDefaults()

      d2 {
        layoutEngine = LayoutEngine.Elk
        fileFormat = FileFormat.Svg
      }
    }
  """.trimIndent()
}
