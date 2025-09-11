/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import modular.test.ModularTaskTest
import modular.test.runTask
import modular.test.scenarios.DotFileBasic
import modular.test.scenarios.DotFileChartCustomConfig
import modular.test.scenarios.DotFileChartWithProperties
import modular.test.scenarios.DotFileChartWithReplacements
import modular.test.scenarios.OneKotlinJvmModule
import kotlin.test.Test

class GenerateModulesDotFileTaskTest : ModularTaskTest() {
  @Test
  fun `Don't register task if no outputs configured`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("tasks").build()

    // then the task didn't exist
    assertThat(result.output).doesNotContain("generateModulesDotFile")
  }

  @Test
  fun `Generate dotfiles from basic config`() = runScenario(DotFileBasic) {
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
          edge ["dir"="forward","arrowhead"="normal","arrowtail"="none"]
          graph ["dpi"="72","fontsize"="30","ranksep"="1.5","rankdir"="TB"]
          node ["style"="filled"]
          ":a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          ":b" ["fillcolor"="#FF8800","shape"="none"]
          ":c" ["fillcolor"="#FF8800","shape"="none"]
          ":a" -> ":b"
          ":a" -> ":c" ["style"="dotted"]
        }
      """.trimIndent(),
    )

    assertThat(dotFileB.readText()).contains(
      """
        digraph {
          edge ["dir"="forward","arrowhead"="normal","arrowtail"="none"]
          graph ["dpi"="72","fontsize"="30","ranksep"="1.5","rankdir"="TB"]
          node ["style"="filled"]
          ":b" ["fillcolor"="#FF8800","color"="black","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
    assertThat(dotFileC.readText()).contains(
      """
        digraph {
          edge ["dir"="forward","arrowhead"="normal","arrowtail"="none"]
          graph ["dpi"="72","fontsize"="30","ranksep"="1.5","rankdir"="TB"]
          node ["style"="filled"]
          ":c" ["fillcolor"="#FF8800","color"="black","penwidth"="3","shape"="box"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from build script`() = runScenario(DotFileChartCustomConfig) {
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
          ":a" -> ":c" ["style"="dotted"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile from gradle properties`() = runScenario(DotFileChartWithProperties) {
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
          ":a" -> ":c" ["style"="dotted"]
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Replace module names`() = runScenario(DotFileChartWithReplacements) {
    // when
    runTask("generateModulesDotFile").build()

    // then the file was generated
    val dotFile = resolve("a/modules.dot")
    assertThat(dotFile).exists()

    // and contains expected contents, colons removed from module prefixes and "b" -> "B"
    assertThat(dotFile.readText()).contains(
      """
        digraph {
          edge ["dir"="forward","arrowhead"="normal","arrowtail"="none"]
          graph ["dpi"="72","fontsize"="30","ranksep"="1.5","rankdir"="TB"]
          node ["style"="filled"]
          "B" ["fillcolor"="#FF8800","shape"="none"]
          "a" ["fillcolor"="#CA66FF","color"="black","penwidth"="3","shape"="box"]
          "c" ["fillcolor"="#FF8800","shape"="none"]
          "a" -> "B"
          "a" -> "c" ["style"="dotted"]
        }
      """.trimIndent(),
    )
  }
}
