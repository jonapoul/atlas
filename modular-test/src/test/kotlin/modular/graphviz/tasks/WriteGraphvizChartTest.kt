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
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class WriteGraphvizChartTest : ScenarioTest() {
  @Test
  fun `Run if no module types are declared`() = runScenario(GraphvizNestedModulesNoModuleTypes) {
    // when
    runTask("writeGraphvizChart").build()

    // then
    assertThat(resolve("app/modular/chart.dot").readText()).equalsDiffed(
      """
        digraph {
          node [style="filled"]
          ":app" [shape="box",penwidth="3"]
          ":data:a" [shape="none"]
          ":data:b" [shape="none"]
          ":domain:a" [shape="none"]
          ":domain:b" [shape="none"]
          ":ui:a" [shape="none"]
          ":ui:b" [shape="none"]
          ":ui:c" [shape="none"]
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
          node [style="filled"]
          ":a" [shape="box",penwidth="3",fillcolor="mediumorchid"]
          ":b" [shape="none",fillcolor="orange"]
          ":c" [shape="none",fillcolor="orange"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )

    assertThat(dotFileB.readText()).equalsDiffed(
      """
        digraph {
          node [style="filled"]
          ":b" [shape="box",penwidth="3",fillcolor="orange"]
        }
      """.trimIndent(),
    )
    assertThat(dotFileC.readText()).equalsDiffed(
      """
        digraph {
          node [style="filled"]
          ":c" [shape="box",penwidth="3",fillcolor="orange"]
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
          edge [dir="none",arrowhead="halfopen",arrowtail="open"]
          graph [dpi="150",fontsize="20",ranksep="2.5",rankdir="LR"]
          node [style="filled"]
          ":a" [shape="box",penwidth="3",fillcolor="mediumorchid"]
          ":b" [shape="none",fillcolor="orange"]
          ":c" [shape="none",fillcolor="orange"]
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
          edge [dir="none",arrowhead="halfopen",arrowtail="open"]
          graph [dpi="150",fontsize="20",ranksep="2.5",rankdir="LR"]
          node [style="filled"]
          ":a" [shape="box",penwidth="3",fillcolor="mediumorchid"]
          ":b" [shape="none",fillcolor="orange"]
          ":c" [shape="none",fillcolor="orange"]
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
          node [style="filled"]
          "B" [shape="none",fillcolor="orange"]
          "a" [shape="box",penwidth="3",fillcolor="mediumorchid"]
          "c" [shape="none",fillcolor="orange"]
          "a" -> "B"
          "a" -> "c"
        }
      """.trimIndent(),
    )
  }

  @Test
  @Disabled("https://github.com/jonapoul/modular/issues/247")
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
          node [style="filled"]
          ":a" [fillcolor="mediumorchid",shape="box",penwidth="3"]
          ":b" [fillcolor="mediumorchid",shape="none"]
          ":c" [fillcolor="orange",shape="none"]
          ":d" [fillcolor="orange",shape="none"]
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
          node [style="filled"]
          ":app" [shape="box",penwidth="3",fillcolor="mediumorchid"]
          ":data:a" [shape="none",fillcolor="mediumorchid"]
          ":data:b" [shape="none",fillcolor="mediumorchid"]
          ":domain:a" [shape="none",fillcolor="mediumorchid"]
          ":domain:b" [shape="none",fillcolor="mediumorchid"]
          ":ui:a" [shape="none",fillcolor="mediumorchid"]
          ":ui:b" [shape="none",fillcolor="mediumorchid"]
          ":ui:c" [shape="none",fillcolor="mediumorchid"]
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
