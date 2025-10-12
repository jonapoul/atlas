/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario

internal object GraphVizBasicWithPngOutput : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.FileFormat

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphviz {
        fileFormat = FileFormat.Png
      }
    }
  """.trimIndent()
}
