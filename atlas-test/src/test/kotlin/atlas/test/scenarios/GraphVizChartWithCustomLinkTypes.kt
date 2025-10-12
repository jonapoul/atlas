/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object GraphVizChartWithCustomLinkTypes : GraphvizScenario {
  override val rootBuildFile = """
    import atlas.graphviz.ArrowType
    import atlas.graphviz.RankDir
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
        api(style = LinkStyle.Bold)
        implementation(color = "blue")
        "compileOnly"(color = "#FF55FF", style = LinkStyle.Dotted)
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
        compileOnly(project(":d"))
      }
    """.trimIndent(),

    "b" to kotlinJvmBuildScript,

    "c" to javaBuildScript,

    "d" to javaBuildScript,
  )
}
