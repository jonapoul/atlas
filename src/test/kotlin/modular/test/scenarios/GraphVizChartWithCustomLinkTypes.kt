/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JAVA_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object GraphVizChartWithCustomLinkTypes : Scenario {
  override val rootBuildFile = """
    import modular.graphviz.spec.ArrowType
    import modular.graphviz.spec.LinkStyle
    import modular.graphviz.spec.RankDir

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphViz()

      linkTypes {
        api(style = LinkStyle.Bold)
        implementation(color = "blue")
        "compileOnly"(color = "#FF55FF", style = "dotted")
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
        compileOnly(project(":d"))
      }
    """.trimIndent(),

    "b" to BASIC_JVM_BUILD_SCRIPT,

    "c" to BASIC_JAVA_BUILD_SCRIPT,

    "d" to BASIC_JAVA_BUILD_SCRIPT,
  )
}
