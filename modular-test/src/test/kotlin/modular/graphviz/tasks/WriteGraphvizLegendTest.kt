/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.contentEquals
import modular.test.runTask
import modular.test.scenarios.GraphVizWithLinkTypes
import modular.test.scenarios.GraphvizBasic
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

internal class WriteGraphvizLegendTest : ScenarioTest() {
  @Test
  internal fun `Generate dotfile legend from basic config`() = runScenario(GraphvizBasic) {
    // when
    runTask("writeGraphvizLegend").build()

    // then the file was generated
    val legendFile = resolve("modular/legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, with modules in declaration order
    assertThat(legendFile.readText()).contains(
      """
        digraph {
          node [shape=plaintext]
          modules [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
            <TR><TD>Kotlin JVM</TD><TD BGCOLOR="mediumorchid">&lt;module-name&gt;</TD></TR>
            <TR><TD>Java</TD><TD BGCOLOR="orange">&lt;module-name&gt;</TD></TR>
            <TR><TD>Custom</TD><TD BGCOLOR="#123456">&lt;module-name&gt;</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  @Test
  @Disabled("https://github.com/jonapoul/modular/issues/247")
  internal fun `Show modules and links next to each other`() = runScenario(GraphVizWithLinkTypes) {
    // when
    runTask("writeGraphvizLegend").build()

    // then the file was generated
    val legendFile = resolve("modular/legend.dot")
    assertThat(legendFile).exists()

    // and contains expected contents, overriding build script
    assertThat(legendFile).contentEquals(
      """
        digraph {
          node [shape=plaintext]
          modules [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
            <TR><TD>Kotlin JVM</TD><TD BGCOLOR="mediumorchid">&lt;module-name&gt;</TD></TR>
            <TR><TD>Java</TD><TD BGCOLOR="orange">&lt;module-name&gt;</TD></TR>
          </TABLE>
          >];
          links [label=<
          <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
            <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Link Types</B></TD></TR>
            <TR><TD>jvmMainImplementation</TD><TD BGCOLOR="orange">Bold</TD></TR>
            <TR><TD>api</TD><TD>Solid</TD></TR>
            <TR><TD>implementation</TD><TD>Dotted</TD></TR>
          </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }
}
