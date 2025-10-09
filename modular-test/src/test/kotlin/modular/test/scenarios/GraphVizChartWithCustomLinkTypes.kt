/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION
import modular.test.javaBuildScript
import modular.test.kotlinJvmBuildScript

object GraphVizChartWithCustomLinkTypes : GraphvizScenario {
  override val rootBuildFile = """
    import modular.graphviz.ArrowType
    import modular.graphviz.RankDir
    import modular.graphviz.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
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
