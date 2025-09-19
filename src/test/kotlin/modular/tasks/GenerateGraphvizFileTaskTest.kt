/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import modular.test.RequiresGraphviz
import modular.test.RequiresLn
import modular.test.RequiresWhereis
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.doesNotExist
import modular.test.runTask
import modular.test.scenarios.GraphVizBasic
import modular.test.scenarios.GraphVizBasicWithThreeOutputFormats
import modular.test.scenarios.GraphVizBigGraph100DpiSvg
import modular.test.scenarios.GraphVizBigGraph100DpiSvgWithAdjustment
import modular.test.scenarios.GraphVizCustomDotExecutable
import modular.test.scenarios.GraphVizCustomLayoutEngine
import modular.test.scenarios.GraphVizInvalidLayoutEngine
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import kotlin.test.Test
import kotlin.text.RegexOption.MULTILINE

class GenerateGraphvizFileTaskTest : ScenarioTest() {
  @Test
  fun `No extras are generated if no file formats have been declared`() = runScenario(GraphVizBasic) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :generateLegendDotFile SKIPPED
        :generateLegendDotFileForChecking SKIPPED
        :modularGenerate SKIPPED
        :a:dumpModuleType SKIPPED
        :b:dumpModuleType SKIPPED
        :c:dumpModuleType SKIPPED
        :collateModuleTypes SKIPPED
        :a:dumpModuleLinks SKIPPED
        :b:dumpModuleLinks SKIPPED
        :c:dumpModuleLinks SKIPPED
        :collateModuleLinks SKIPPED
        :a:calculateModuleTree SKIPPED
        :a:generateChartDotFile SKIPPED
        :a:generateChartDotFileForChecking SKIPPED
        :a:modularGenerate SKIPPED
        :b:calculateModuleTree SKIPPED
        :b:generateChartDotFile SKIPPED
        :b:generateChartDotFileForChecking SKIPPED
        :b:modularGenerate SKIPPED
        :c:calculateModuleTree SKIPPED
        :c:generateChartDotFile SKIPPED
        :c:generateChartDotFileForChecking SKIPPED
        :c:modularGenerate SKIPPED

        BUILD SUCCESSFUL
      """.trimIndent(),
    )
  }

  @Test
  @RequiresGraphviz
  fun `Generate png, svg and eps files`() = runScenario(GraphVizBasicWithThreeOutputFormats) {
    // when
    val result = runTask("modularGenerate").build()

    // then PNG, SVG and EPS tasks were run for each submodule
    listOf(
      ":a:generateChartPng",
      ":a:generateChartSvg",
      ":a:generateChartEps",
      ":b:generateChartPng",
      ":b:generateChartSvg",
      ":b:generateChartEps",
      ":c:generateChartPng",
      ":c:generateChartSvg",
      ":c:generateChartEps",
    ).forEach { t ->
      assertThat(result.task(t)?.outcome).isEqualTo(SUCCESS)
    }

    // and the relevant files exist
    for (submodule in listOf("a", "b", "c")) {
      assertThat(resolve("$submodule/modules.png")).exists()
      assertThat(resolve("$submodule/modules.svg")).exists()
      assertThat(resolve("$submodule/modules.eps")).exists()
    }
  }

  @Test
  @RequiresGraphviz
  fun `SVG viewBox scales with nondefault DPI with flag enabled`() =
    runScenario(GraphVizBigGraph100DpiSvgWithAdjustment) {
      // when
      runTask(":app:generateChartSvg").build()

      // then the app graph was generated as an svg
      val contents = resolve("app/modules.svg").readText()

      // and the viewBox matches the width/height
      assertThat(contents).contains(
        """
        <svg width="486pt" height="361pt"
        """.trimIndent(),
      )
      assertThat(contents).contains(
        """
        viewBox="0.00 0.00 486.00 361.00"
        """.trimIndent(),
      )
    }

  @Test
  @RequiresGraphviz
  fun `SVG doesn't scale viewBox if experimental flag is unset`() = runScenario(GraphVizBigGraph100DpiSvg) {
    // when
    runTask(":app:generateChartSvg").build()

    // then the app graph was generated as an svg
    val contents = resolve("app/modules.svg").readText()

    // and the viewBox doesn't match the width/height
    assertThat(contents).contains(
      """
        <svg width="486pt" height="361pt"
      """.trimIndent(),
    )
    assertThat(contents).contains(
      """
        viewBox="0.00 0.00 350.00 260.00"
      """.trimIndent(),
    )
  }

  @Test
  @RequiresGraphviz
  fun `Fail with useful message for invalid layout engine`() = runScenario(GraphVizInvalidLayoutEngine) {
    // when
    val result = runTask(":app:generateChartSvg").buildAndFail()

    // then the error log contains a useful message from graphviz
    assertThat(result.output).contains("There is no layout engine support for \"abc123\"")

    // on my machine the bit following this is "circo dot fdp neato nop nop1 nop2 osage patchwork sfdp twopi", but
    // it'll probs be installation-dependent
    assertThat(result.output).contains("Use one of: ")
  }

  @Test
  @RequiresGraphviz
  fun `Choose custom layout engine`() = runScenario(GraphVizCustomLayoutEngine) {
    // when we specify the "neato" layout engine
    val result = runTask(":app:generateChartSvg").build()

    // then the SVG file printed in the log exists. There's probably more I can be checking here but ¯\_(ツ)_/¯
    assertThat(result.output).containsMatch("^(.*?\\.svg)$".toRegex(MULTILINE))
  }

  @Test
  @RequiresGraphviz
  @RequiresLn
  @RequiresWhereis
  fun `Use custom path to dot command`() = runScenario(GraphVizCustomDotExecutable) {
    // Given we've made a symbolic link to a dot executable
    val whereisProcess = ProcessBuilder("whereis", "dot").start()
    val pathToDot = whereisProcess.inputReader().readLine().split(" ")[1]
    assertThat(whereisProcess.waitFor()).isEqualTo(0)
    val customDotFile = resolve("path/to/custom/dot")
    customDotFile.parentFile.mkdirs()
    val lnProcess = ProcessBuilder("ln", "-s", pathToDot, customDotFile.absolutePath).start()
    assertThat(lnProcess.waitFor()).isEqualTo(0)
    assertThat(customDotFile).exists()

    // when
    val result = runTask("modularGenerate").build()

    // then it's all good
    assertThat(result.tasks).allSuccessful()

    // if we don't add this we'll get a junit log warning
    customDotFile.delete()
  }

  @Test
  @RequiresGraphviz
  fun `Fail with nonexistent custom path to dot command`() = runScenario(GraphVizCustomDotExecutable) {
    // Given we've made a symbolic link to a dot executable which doesn't exist
    val customDotFile = resolve("path/to/custom/dot")
    assertThat(customDotFile).doesNotExist()

    // when
    val result = runTask("modularGenerate").buildAndFail()

    // then it fails as expected
    assertThat(result.output).containsMatch(
      """
        Execution failed for task ':a:generateChartSvg'.
        > java.io.IOException: Cannot run program ".*?/path/to/custom/dot": error=2, No such file or directory
      """.trimIndent().toRegex(),
    )
  }
}
