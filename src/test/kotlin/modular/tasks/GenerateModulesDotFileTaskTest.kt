/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

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
import modular.test.scenarios.OneKotlinJvmModule
import kotlin.test.Test

class GenerateModulesDotFileTaskTest : ScenarioTest() {
  @Test
  fun `Don't register task if no outputs configured`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("tasks").build()

    // then the task didn't exist
    assertThat(result.output).doesNotContain("generateModulesDotFile")
  }

  @Test
  fun `Generate dotfiles from basic config`() = runScenario(GraphVizBasic) {
    // when
    runTask("generateModulesDotFile").build()

    // then the files were generated
    val dotFileA = resolve("a/modules.dot")
    val dotFileB = resolve("b/modules.dot")
    val dotFileC = resolve("c/modules.dot")

    // and contain expected contents, with modules in declaration order
    assertThat(dotFileA.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="#FF8800","shape"="none"]
          ":c" ["fillcolor"="#FF8800","shape"="none"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )

    assertThat(dotFileB.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":b" ["fillcolor"="#FF8800","color"="black","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
    assertThat(dotFileC.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":c" ["fillcolor"="#FF8800","color"="black","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from build script`() = runScenario(GraphVizChartCustomConfig) {
    // when
    runTask("generateModulesDotFile").build()

    // then the file was generated
    val dotFile = resolve("a/modules.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          edge ["dir"="none","arrowhead"="halfopen","arrowtail"="open"]
          graph ["dpi"="150","fontsize"="20","ranksep"="2.5","rankdir"="LR"]
          node ["style"="filled"]
          ":a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="#FF8800","shape"="none"]
          ":c" ["fillcolor"="#FF8800","shape"="none"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from gradle properties`() = runScenario(GraphVizChartWithProperties) {
    // when
    runTask("generateModulesDotFile").build()

    // then the file was generated
    val dotFile = resolve("a/modules.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, with modules in alphabetical order
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          edge ["dir"="none","arrowhead"="halfopen","arrowtail"="open"]
          graph ["dpi"="150","fontsize"="20","ranksep"="2.5","rankdir"="LR"]
          node ["style"="filled"]
          ":a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="#FF8800","shape"="none"]
          ":c" ["fillcolor"="#FF8800","shape"="none"]
          ":a" -> ":b"
          ":a" -> ":c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Replace module names`() = runScenario(GraphVizChartWithReplacements) {
    // when
    runTask("generateModulesDotFile").build()

    // then the file was generated
    val dotFile = resolve("a/modules.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, colons removed from module prefixes and "b" -> "B"
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          "B" ["fillcolor"="#FF8800","shape"="none"]
          "a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          "c" ["fillcolor"="#FF8800","shape"="none"]
          "a" -> "B"
          "a" -> "c"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Handle custom link types`() = runScenario(GraphVizChartWithCustomLinkTypes) {
    // when
    runTask("generateModulesDotFile").build()

    // then the file was generated
    val dotFile = resolve("a/modules.dot")
    assertThat(dotFile).exists()

    // and contains expected link styles
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="#CA66FF","shape"="none"]
          ":c" ["fillcolor"="#FF8800","shape"="none"]
          ":d" ["fillcolor"="#FF8800","shape"="none"]
          ":a" -> ":b" ["style"="bold"]
          ":a" -> ":c" ["color"="blue"]
          ":a" -> ":d" ["style"="dotted","color"="#FF55FF"]
        }
      """.trimIndent(),
    )
  }
}
