/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import modular.graphviz.RequiresGraphviz
import modular.test.RequiresLn
import modular.test.RequiresWhereis
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.doesNotExist
import modular.test.runTask
import modular.test.scenarios.GraphVizBasicWithPngOutput
import modular.test.scenarios.GraphVizBigGraph100DpiSvg
import modular.test.scenarios.GraphVizBigGraph100DpiSvgWithAdjustment
import modular.test.scenarios.GraphVizCustomDotExecutable
import modular.test.scenarios.GraphVizCustomLayoutEngine
import modular.test.scenarios.GraphvizBasic
import org.gradle.testkit.runner.TaskOutcome
import kotlin.test.Test

class ExecGraphvizTest : ScenarioTest() {
  @Test
  fun `No extras are generated if no file formats have been declared`() = runScenario(GraphvizBasic) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :writeGraphvizLegend SKIPPED
        :execGraphvizLegend SKIPPED
        :modularGenerate SKIPPED
        :a:writeModuleType SKIPPED
        :b:writeModuleType SKIPPED
        :c:writeModuleType SKIPPED
        :collateModuleTypes SKIPPED
        :a:writeModuleLinks SKIPPED
        :b:writeModuleLinks SKIPPED
        :c:writeModuleLinks SKIPPED
        :collateModuleLinks SKIPPED
        :a:calculateModuleTree SKIPPED
        :a:writeGraphvizChart SKIPPED
        :a:execGraphvizChart SKIPPED
        :a:writeGraphvizReadme SKIPPED
        :a:modularGenerate SKIPPED
        :b:calculateModuleTree SKIPPED
        :b:writeGraphvizChart SKIPPED
        :b:execGraphvizChart SKIPPED
        :b:writeGraphvizReadme SKIPPED
        :b:modularGenerate SKIPPED
        :c:calculateModuleTree SKIPPED
        :c:writeGraphvizChart SKIPPED
        :c:execGraphvizChart SKIPPED
        :c:writeGraphvizReadme SKIPPED
        :c:modularGenerate SKIPPED

        BUILD SUCCESSFUL
      """.trimIndent(),
    )
  }

  @Test
  @RequiresGraphviz
  fun `Generate png, svg and eps files`() = runScenario(GraphVizBasicWithPngOutput) {
    // when
    val result = runTask("modularGenerate").build()

    // then PNG, SVG and EPS tasks were run for each submodule
    listOf(
      ":a:execGraphvizChart",
      ":b:execGraphvizChart",
      ":c:execGraphvizChart",
    ).forEach { t ->
      assertThat(result.task(t)?.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    // and the relevant files exist
    for (submodule in listOf("a", "b", "c")) {
      assertThat(resolve("$submodule/modular/chart.png")).exists()
    }
  }

  @Test
  @RequiresGraphviz
  fun `SVG viewBox scales with nondefault DPI with flag enabled`() =
    runScenario(GraphVizBigGraph100DpiSvgWithAdjustment) {
      // when
      runTask(":app:execGraphvizChart").build()

      // then the app graph was generated as an svg
      val contents = resolve("app/modular/chart.svg").readText()

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
    runTask(":app:execGraphvizChart").build()

    // then the app graph was generated as an svg
    val contents = resolve("app/modular/chart.svg").readText()

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
  fun `Choose custom layout engine`() = runScenario(GraphVizCustomLayoutEngine) {
    // when we specify the "neato" layout engine
    val result = runTask(":app:execGraphvizChart").build()

    // There's probably more I can be checking here but ¯\_(ツ)_/¯
    assertThat(result.tasks).allSuccessful()
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
        > A problem occurred starting process 'command '.*?/path/to/custom/dot''
      """.trimIndent().toRegex(),
    )
  }
}
