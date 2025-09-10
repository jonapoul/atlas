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
import modular.test.doesNotExist
import modular.test.runTask
import modular.test.scenarios.DotFileBasic
import modular.test.scenarios.DotFileBasicWithBasicLegend
import modular.test.scenarios.DotFileLegendCustomConfig
import modular.test.scenarios.DotFileLegendWithProperties
import modular.test.scenarios.OneKotlinJvmModule
import kotlin.test.Test

class GenerateLegendDotFileTaskTest : ModularTaskTest() {
  @Test
  fun `Don't register legend task if no outputs are configured`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("tasks").build()

    // then the task didn't exist
    assertThat(result.output).doesNotContain("generateLegendDotFile")
  }

  @Test
  fun `Don't register legend task if legend wasnt configured`() = runScenario(DotFileBasic) {
    // when
    val result = runTask("generateLegendDotFile").buildAndFail()

    // then the task wasn't run because it wasnt registered
    assertThat(result.output).contains("Task 'generateLegendDotFile' not found in root project")

    // and the legend file wasn't generated
    val legendFile = resolve("legend.dot")
    assertThat(legendFile).doesNotExist()
  }

  @Test
  fun `Generate dotfile legend from basic config`() = runScenario(DotFileBasicWithBasicLegend) {
    // when
    runTask("generateLegendDotFile").build()

    // then the file was generated
    val legendFile = resolve("legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, with modules in declaration order
    assertThat(legendFile.readText()).contains(
      """
        digraph {
          node [shape=plaintext]
          table1 [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
          <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">module-name</TD></TR>
          <TR><TD>Java</TD><TD BGCOLOR="#FF8800">module-name</TD></TR>
          <TR><TD>Custom</TD><TD BGCOLOR="#123456">module-name</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile legend from build script`() = runScenario(DotFileLegendCustomConfig) {
    // when
    runTask("generateLegendDotFile").build()

    // then the file was generated
    val legendFile = resolve("legend-dir/legend-filename.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, with modules in declaration order
    assertThat(legendFile.readText()).contains(
      """
        digraph {
          node [shape=plaintext]
          table1 [label=<
          <TABLE BORDER="5" CELLBORDER="2" CELLSPACING="4" CELLPADDING="3">
          <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">module-name</TD></TR>
          <TR><TD>Java</TD><TD BGCOLOR="#FF8800">module-name</TD></TR>
          <TR><TD>Custom</TD><TD BGCOLOR="#123456">module-name</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Customise dotfile legend from gradle properties`() = runScenario(DotFileLegendWithProperties) {
    // when
    runTask("generateLegendDotFile").build()

    // then the file was generated
    val legendFile = resolve("legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, overriding build script
    assertThat(legendFile.readText()).contains(
      """
        digraph {
          node [shape=plaintext]
          table1 [label=<
          <TABLE BORDER="10" CELLBORDER="20" CELLSPACING="30" CELLPADDING="40">
          <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">module-name</TD></TR>
          <TR><TD>Java</TD><TD BGCOLOR="#FF8800">module-name</TD></TR>
          <TR><TD>Custom</TD><TD BGCOLOR="#123456">module-name</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }
}
