/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAtLeast
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import atlas.test.ScenarioTest
import atlas.test.runTask
import atlas.test.scenarios.CheckExplicitlyDisabled
import atlas.test.scenarios.CheckExplicitlyEnabled
import atlas.test.scenarios.D2Basic
import atlas.test.scenarios.GraphVizBasicWithPngOutput
import atlas.test.scenarios.GraphvizBasic
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.Test

internal class CheckFileDiffTest : ScenarioTest() {
  @Test
  fun `Write doesn't run as a dependency of check for graphviz`() = runScenario(GraphvizBasic) {
    // when
    val result = runTask(":a:checkGraphvizChart", extras = listOf("--dry-run")).build()

    // then the chart wasn't written
    assertThat(result.output).doesNotContain(":a:writeGraphvizChart")

    // but the dummy and check tasks were run
    assertThat(result.output).contains(":a:writeDummyGraphvizChart")
    assertThat(result.output).contains(":a:checkGraphvizChart")
  }

  @Test
  fun `Write doesn't run as a dependency of check for D2`() = runScenario(D2Basic) {
    // when
    val result = runTask(":a:checkD2Chart", extras = listOf("--dry-run")).build()

    // then the chart wasn't written
    assertThat(result.output).doesNotContain(":a:writeD2Chart")

    // but the dummy and check tasks were run
    assertThat(result.output).contains(":a:writeDummyD2Chart")
    assertThat(result.output).contains(":a:checkD2Chart")
  }

  @Test
  fun `Fail if the expected file hasn't been generated yet`() = runScenario(GraphvizBasic) {
    // when
    val result = runTask(":a:checkGraphvizChart").buildAndFail()

    // then
    assertThat(result.output).contains(
      """
        * What went wrong:
        Execution failed for task ':a:checkGraphvizChart'.
        > java.io.FileNotFoundException
      """.trimIndent(),
    )
    assertThat(result.output).contains(
      """
        * Try:
        > Run `gradle :a:writeGraphvizChart` to generate the file.
      """.trimIndent(),
    )
  }

  @Test
  fun `Verify modules of a basic project`() = runScenario(GraphVizBasicWithPngOutput) {
    // given initial dotfile is generated
    runTask(":a:writeGraphvizChart").build()

    // when we check it with no changes
    val check1 = runTask(":a:checkGraphvizChart").build()

    // then the check was successful
    assertThat(check1.task(":a:checkGraphvizChart")?.outcome).isEqualTo(SUCCESS)

    // given we set a custom property set to adjust the output
    resolve("gradle.properties").writeText("atlas.graphviz.layoutEngine=circo")

    // when we run a check again
    val check2 = runTask(":a:checkGraphvizChart").buildAndFail()

    // then it fails
    assertThat(check2.task(":a:checkGraphvizChart")?.outcome).isEqualTo(FAILED)

    // and spits out expected diff
    assertThat(check2.output).contains(
      """
      |          digraph {
      |      ---   graph [layout="circo"]
      |            ":a" [fillcolor="mediumorchid"]
      |            ":b" [fillcolor="orange"]
      |            ":c" [fillcolor="orange"]
      |            ":a" -> ":b"
      |            ":a" -> ":c"
      |          }
      """.trimMargin(),
    )
  }

  @Test
  fun `Verify legend of a basic project`() = runScenario(GraphvizBasic) {
    // given initial dotfile is generated
    runTask("writeGraphvizLegend").build()

    // when we check it with no changes
    val check1 = runTask("checkGraphvizLegend").build()

    // then the check was successful
    assertThat(check1.task(":checkGraphvizLegend")?.outcome).isEqualTo(SUCCESS)

    // given we manually adjust the generated file
    val legendFile = resolve("atlas/legend.dot")
    val editedLegend = legendFile
      .readText()
      .replace("CELLBORDER=\"1\"", "CELLBORDER=\"100\"")
    legendFile.writeText(editedLegend)

    // when we run a check again
    val check2 = runTask("checkGraphvizLegend").buildAndFail()

    // then it fails
    assertThat(check2.task(":checkGraphvizLegend")?.outcome).isEqualTo(FAILED)

    // and spits out expected diff
    assertThat(check2.output).contains(
      """
        |          digraph {
        |            node [shape=plaintext]
        |            modules [label=<
        |      ---   <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
        |      +++   <TABLE BORDER="0" CELLBORDER="100" CELLSPACING="0" CELLPADDING="4">
        |              <TR><TD COLSPAN="2" BGCOLOR="#DDDDDD"><B>Module Types</B></TD></TR>
        |              <TR><TD>Kotlin JVM</TD><TD BGCOLOR="mediumorchid">&lt;module-name&gt;</TD></TR>
        |              <TR><TD>Java</TD><TD BGCOLOR="orange">&lt;module-name&gt;</TD></TR>
        |              <TR><TD>Custom</TD><TD BGCOLOR="#123456">&lt;module-name&gt;</TD></TR>
        |            </TABLE>
        |            >];
        |          }
      """.trimMargin(),
    )
  }

  @Test
  fun `Register check tasks when checkOutputs is true`() = runScenario(CheckExplicitlyEnabled) {
    // when
    val result = runTask("check", extras = listOf("--dry-run")).build()
    val output = result.output.lines()

    // then
    assertThat(output).containsAtLeast(
      ":check SKIPPED",
      ":checkGraphvizLegend SKIPPED",
      ":a:checkGraphvizChart SKIPPED",
      ":b:checkGraphvizChart SKIPPED",
      ":c:checkGraphvizChart SKIPPED",
    )
  }

  @Test
  fun `Don't register check tasks when checkOutputs is false`() = runScenario(CheckExplicitlyDisabled) {
    // when
    val result = runTask("check", extras = listOf("--dry-run")).build()

    // then
    assertThat(result.output).doesNotContain("checkGraphvizLegend")
  }
}
