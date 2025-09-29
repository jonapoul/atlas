/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.GraphVizBasic
import modular.test.scenarios.GraphVizChartCustomConfig
import modular.test.scenarios.GraphVizChartWithCustomLinkTypes
import modular.test.scenarios.GraphVizChartWithProperties
import modular.test.scenarios.GraphVizChartWithReplacements
import modular.test.scenarios.NestedModules
import modular.test.scenarios.NestedModulesNoModuleTypes
import modular.test.scenarios.OneKotlinJvmModule
import kotlin.test.Test

class WriteDotChartTest : ScenarioTest() {
  @Test
  fun `Don't register task if no outputs configured`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("tasks").build()

    // then the task didn't exist
    assertThat(result.output).doesNotContain("writeGraphvizChart")
  }

  @Test
  fun `Run if no module types are declared`() = runScenario(NestedModulesNoModuleTypes) {
    // when
    runTask("writeGraphvizChart").build()

    // then
    assertThat(resolve("app/chart.dot").readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":app" ["penwidth"="3","shape"="box"]
          ":data:a" ["shape"="none"]
          ":data:b" ["shape"="none"]
          ":domain:a" ["shape"="none"]
          ":domain:b" ["shape"="none"]
          ":ui:a" ["shape"="none"]
          ":ui:b" ["shape"="none"]
          ":ui:c" ["shape"="none"]
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
  fun `Generate dotfiles from basic config`() = runScenario(GraphVizBasic) {
    // when
    runTask("writeGraphvizChart").build()

    // then the files were generated
    val dotFileA = resolve("a/chart.dot")
    val dotFileB = resolve("b/chart.dot")
    val dotFileC = resolve("c/chart.dot")

    // and contain expected contents, with modules in declaration order
    assertThat(dotFileA.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":a" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="orange","shape"="none"]
          ":c" ["fillcolor"="orange","shape"="none"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )

    assertThat(dotFileB.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":b" ["fillcolor"="orange","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
    assertThat(dotFileC.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":c" ["fillcolor"="orange","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from build script`() = runScenario(GraphVizChartCustomConfig) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("a/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          edge ["dir"="none","arrowhead"="halfopen","arrowtail"="open"]
          graph ["dpi"="150","fontsize"="20","ranksep"="2.5","rankdir"="LR"]
          node ["style"="filled"]
          ":a" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="orange","shape"="none"]
          ":c" ["fillcolor"="orange","shape"="none"]
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
    val dotFile = resolve("a/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          edge ["dir"="none","arrowhead"="halfopen","arrowtail"="open"]
          graph ["dpi"="150","fontsize"="20","ranksep"="2.5","rankdir"="LR"]
          node ["style"="filled"]
          ":a" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="orange","shape"="none"]
          ":c" ["fillcolor"="orange","shape"="none"]
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
    val dotFile = resolve("a/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, colons removed from module prefixes and "b" -> "B"
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          "B" ["fillcolor"="orange","shape"="none"]
          "a" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          "c" ["fillcolor"="orange","shape"="none"]
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
    val dotFile = resolve("a/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected link styles
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":a" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="mediumorchid","shape"="none"]
          ":c" ["fillcolor"="orange","shape"="none"]
          ":d" ["fillcolor"="orange","shape"="none"]
          ":a" -> ":b" ["style"="bold"]
          ":a" -> ":c" ["color"="blue"]
          ":a" -> ":d" ["style"="dotted","color"="#FF55FF"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Handle nested modules`() = runScenario(NestedModules) {
    // when
    runTask("writeGraphvizChart").build()

    // then the file was generated
    val dotFile = resolve("app/chart.dot")
    assertThat(dotFile).exists()

    // and contains expected link styles
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":app" ["fillcolor"="mediumorchid","penwidth"="3","shape"="box"]
          ":data:a" ["fillcolor"="mediumorchid","shape"="none"]
          ":data:b" ["fillcolor"="mediumorchid","shape"="none"]
          ":domain:a" ["fillcolor"="mediumorchid","shape"="none"]
          ":domain:b" ["fillcolor"="mediumorchid","shape"="none"]
          ":ui:a" ["fillcolor"="mediumorchid","shape"="none"]
          ":ui:b" ["fillcolor"="mediumorchid","shape"="none"]
          ":ui:c" ["fillcolor"="mediumorchid","shape"="none"]
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
