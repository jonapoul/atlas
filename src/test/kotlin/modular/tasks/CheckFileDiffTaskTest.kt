/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.GraphVizBasic
import modular.test.scenarios.GraphVizBasicWithThreeOutputFormats
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import kotlin.test.Test

class CheckFileDiffTaskTest : ScenarioTest() {
  @Test
  fun `Verify modules of a basic project`() = runScenario(GraphVizBasicWithThreeOutputFormats) {
    // given initial dotfile is generated
    runTask(":a:generateChartDotFile").build()

    // when we check it with no changes
    val check1 = runTask(":a:checkModulesDotFile").build()

    // then the check was successful
    assertThat(check1.task(":a:checkModulesDotFile")?.outcome).isEqualTo(SUCCESS)

    // given we set a custom property set to adjust the output
    resolve("gradle.properties").writeText("modular.graphViz.chart.layoutEngine=circo")

    // when we run a check again
    val check2 = runTask(":a:checkModulesDotFile").buildAndFail()

    // then it fails
    assertThat(check2.task(":a:checkModulesDotFile")?.outcome).isEqualTo(FAILED)

    // and spits out expected diff
    assertThat(check2.output).contains(
      """
      |      digraph {
      |  +++   graph ["layout"="circo"]
      |        node ["style"="filled"]
      |        ":a" ["fillcolor"="#CA66FF","penwidth"="3","shape"="box"]
      |        ":b" ["fillcolor"="#FF8800","shape"="none"]
      |        ":c" ["fillcolor"="#FF8800","shape"="none"]
      |        ":a" -> ":b"
      |        ":a" -> ":c"
      |      }
      """.trimMargin(),
    )
  }

  @Test
  fun `Verify legend of a basic project`() = runScenario(GraphVizBasic) {
    // given initial dotfile is generated
    runTask("generateLegendDotFile").build()

    // when we check it with no changes
    val check1 = runTask("checkLegendDotFile").build()

    // then the check was successful
    assertThat(check1.task(":checkLegendDotFile")?.outcome).isEqualTo(SUCCESS)

    // given we manually adjust the generated file
    val legendFile = resolve("legend.dot")
    val editedLegend = legendFile
      .readText()
      .replace("CELLBORDER=\"1\"", "CELLBORDER=\"100\"")
    legendFile.writeText(editedLegend)

    // when we run a check again
    val check2 = runTask("checkLegendDotFile").buildAndFail()

    // then it fails
    assertThat(check2.task(":checkLegendDotFile")?.outcome).isEqualTo(FAILED)

    // and spits out expected diff
    assertThat(check2.output).contains(
      """
        |      digraph {
        |        node [shape=plaintext]
        |        modules [label=<
        |  ---   <TABLE BORDER="0" CELLBORDER="100" CELLSPACING="0" CELLPADDING="4">
        |  +++   <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
        |          <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
        |          <TR><TD>Kotlin JVM</TD><TD BGCOLOR="#CA66FF">&lt;module-name&gt;</TD></TR>
        |          <TR><TD>Java</TD><TD BGCOLOR="#FF8800">&lt;module-name&gt;</TD></TR>
        |          <TR><TD>Custom</TD><TD BGCOLOR="#123456">&lt;module-name&gt;</TD></TR>
        |        </TABLE>
        |        >];
        |      }
      """.trimMargin(),
    )
  }
}
