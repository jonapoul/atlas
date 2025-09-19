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
import modular.test.contentEquals
import modular.test.runTask
import modular.test.scenarios.GraphVizBasic
import modular.test.scenarios.GraphVizWithLinkTypes
import modular.test.scenarios.OneKotlinJvmModule
import kotlin.test.Test

class WriteGraphvizChartTest : ScenarioTest() {
  @Test
  fun `Don't register legend task if no outputs are configured`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("tasks").build()

    // then the task didn't exist
    assertThat(result.output).doesNotContain("writeGraphvizLegend")
  }

  @Test
  fun `Generate dotfile legend from basic config`() = runScenario(GraphVizBasic) {
    // when
    runTask("writeGraphvizLegend").build()

    // then the file was generated
    val legendFile = resolve("legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, with modules in declaration order
    assertThat(legendFile.readText()).contains(
      """
        digraph {
          node [shape=plaintext]
          modules [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
            <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">&lt;module-name&gt;</TD></TR>
            <TR><TD>Java</TD><TD BGCOLOR="#FF8800">&lt;module-name&gt;</TD></TR>
            <TR><TD>Custom</TD><TD BGCOLOR="#123456">&lt;module-name&gt;</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Show modules and links next to each other`() = runScenario(GraphVizWithLinkTypes) {
    // when
    runTask("writeGraphvizLegend").build()

    // then the file was generated
    val legendFile = resolve("legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, overriding build script
    assertThat(legendFile).contentEquals(
      """
        digraph {
          node [shape=plaintext]
          modules [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
            <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">&lt;module-name&gt;</TD></TR>
            <TR><TD>Java</TD><TD BGCOLOR="#FF8800">&lt;module-name&gt;</TD></TR>
          </TABLE>
          >];
          links [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Link Types</B></TD></TR>
            <TR><TD>jvmMainImplementation</TD><TD BGCOLOR="orange">bold</TD></TR>
            <TR><TD>.*?api</TD><TD>&lt;style&gt;</TD></TR>
            <TR><TD>.*?implementation</TD><TD>dotted</TD></TR>
          </TABLE>
          >];
        }

      """.trimIndent(),
    )
  }
}
