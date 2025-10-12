/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import assertk.assertThat
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.equalsDiffed
import modular.test.runTask
import modular.test.scenarios.GraphVizChartCustomConfig
import modular.test.scenarios.GraphVizChartWithCustomLinkTypes
import modular.test.scenarios.GraphVizChartWithProperties
import modular.test.scenarios.GraphVizChartWithReplacements
import modular.test.scenarios.GraphvizBasic
import modular.test.scenarios.GraphvizNestedModules
import modular.test.scenarios.GraphvizNestedModulesNoModuleTypes
import kotlin.test.Test

internal class WriteGraphvizChartTest : ScenarioTest() {
  @Test
  fun `Run if no module types are declared`() = runScenario(GraphvizNestedModulesNoModuleTypes) {
    // when
    runTask("writeGraphvizChart").build()

    // then
    assertThat(resolve("app/modular/chart.dot").readText()).equalsDiffed(
      """
        digraph {
          ":app"
          ":data:a"
          ":data:b"
          ":domain:a"
          ":domain:b"
          ":ui:a"
          ":ui:b"
          ":ui:c"
          ":app" -> ":ui:a"
          ":app" -> ":ui:b"
          ":app" -> ":ui:c"
          ":domain:a" -> ":data:a"
          ":domain:b" -> ":data:a"
          ":domain:b" -> ":data:b"
          ":ui:a" -> ":domain:a"
          ":ui:b" -> ":domain:b"
          ":ui:c" -> ":domain:a"
          ":ui:c" -> ":domain:b"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Generate dotfiles from basic config`() = runScenario(GraphvizBasic) {
    // when
    runTask("writeGraphvizChart").build()

    // then the files were generated
    val dotFileA = resolve("a/modular/chart.dot")
    val dotFileB = resolve("b/modular/chart.dot")
    val dotFileC = resolve("c/modular/chart.dot")

    // and contain expected contents, with modules in declaration order
    assertThat(dotFileA.readText()).equalsDiffed(
      """
        digraph {
          ":a" [fillcolor="mediumorchid"]
          ":b" [fillcolor="orange"]
          ":c" [fillcolor="orange"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )

    assertThat(dotFileB.readText()).equalsDiffed(
      """
        digraph {
          ":b" [fillcolor="orange"]
        }
      """.trimIndent(),
    )
    assertThat(dotFileC.readText()).equalsDiffed(
      """
        digraph {
          ":c" [fillcolor="orange"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from build script`() = runScenario(GraphVizChartCustomConfig) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("a/modular/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).equalsDiffed(
      """
        digraph {
          edge [arrowhead="halfopen",arrowtail="open"]
          graph [layout="twopi",dpi="150"]
          node [shape="none"]
          ":a" [fillcolor="mediumorchid"]
          ":b" [fillcolor="orange"]
          ":c" [fillcolor="orange"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from gradle properties`() = runScenario(GraphVizChartWithProperties) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("a/modular/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).equalsDiffed(
      """
        digraph {
          graph [layout="neato"]
          ":a" [fillcolor="mediumorchid"]
          ":b" [fillcolor="orange"]
          ":c" [fillcolor="orange"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Replace module names`() = runScenario(GraphVizChartWithReplacements) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("a/modular/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, colons removed from module prefixes and "b" -> "B"
    assertThat(dotFile.readText()).equalsDiffed(
      """
        digraph {
          "B" [fillcolor="orange"]
          "a" [fillcolor="mediumorchid"]
          "c" [fillcolor="orange"]
          "a" -> "B"
          "a" -> "c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Handle custom link types`() = runScenario(GraphVizChartWithCustomLinkTypes) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("a/modular/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected link styles
    assertThat(dotFile.readText()).equalsDiffed(
      """
        digraph {
          ":a" [fillcolor="mediumorchid"]
          ":b" [fillcolor="mediumorchid"]
          ":c" [fillcolor="orange"]
          ":d" [fillcolor="orange"]
          ":a" -> ":b" [style="bold"]
          ":a" -> ":c" [color="blue"]
          ":a" -> ":d" [style="dotted",color="#FF55FF"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Handle nested modules`() = runScenario(GraphvizNestedModules) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("app/modular/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected link styles
    assertThat(dotFile.readText()).equalsDiffed(
      """
        digraph {
          ":app" [fillcolor="mediumorchid"]
          ":data:a" [fillcolor="mediumorchid"]
          ":data:b" [fillcolor="mediumorchid"]
          ":domain:a" [fillcolor="mediumorchid"]
          ":domain:b" [fillcolor="mediumorchid"]
          ":ui:a" [fillcolor="mediumorchid"]
          ":ui:b" [fillcolor="mediumorchid"]
          ":ui:c" [fillcolor="mediumorchid"]
          ":app" -> ":ui:a"
          ":app" -> ":ui:b"
          ":app" -> ":ui:c"
          ":domain:a" -> ":data:a"
          ":domain:b" -> ":data:a"
          ":domain:b" -> ":data:b"
          ":ui:a" -> ":domain:a"
          ":ui:b" -> ":domain:b"
          ":ui:c" -> ":domain:a"
          ":ui:c" -> ":domain:b"
        }
      """.trimIndent(),
    )
  }
}
