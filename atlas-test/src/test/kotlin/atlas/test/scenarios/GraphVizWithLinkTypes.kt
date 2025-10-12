/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizWithLinkTypes : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        kotlinJvm()
        java()
      }

      linkTypes {
        "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
        api()
        implementation(LinkStyle.Dotted)
      }
    }
  """.trimIndent()
}
