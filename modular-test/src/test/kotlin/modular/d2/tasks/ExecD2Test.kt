/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import modular.d2.RequiresD2
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.runTask
import modular.test.scenarios.D2Basic
import modular.test.scenarios.D2CustomLayoutEngine
import modular.test.taskHadResult
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import kotlin.test.Test

internal class ExecD2Test : ScenarioTest() {
  @Test
  fun `No extras are generated if no file formats have been declared`() = runScenario(D2Basic) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :writeD2Classes SKIPPED
        :modularGenerate SKIPPED
        :a:writeModuleType SKIPPED
        :b:writeModuleType SKIPPED
        :c:writeModuleType SKIPPED
        :collateModuleTypes SKIPPED
        :a:writeModuleLinks SKIPPED
        :b:writeModuleLinks SKIPPED
        :c:writeModuleLinks SKIPPED
        :collateModuleLinks SKIPPED
        :a:writeModuleTree SKIPPED
        :a:writeD2Chart SKIPPED
        :a:execD2Chart SKIPPED
        :a:writeD2Readme SKIPPED
        :a:modularGenerate SKIPPED
        :b:writeModuleTree SKIPPED
        :b:writeD2Chart SKIPPED
        :b:execD2Chart SKIPPED
        :b:writeD2Readme SKIPPED
        :b:modularGenerate SKIPPED
        :c:writeModuleTree SKIPPED
        :c:writeD2Chart SKIPPED
        :c:execD2Chart SKIPPED
        :c:writeD2Readme SKIPPED
        :c:modularGenerate SKIPPED

        BUILD SUCCESSFUL
      """.trimIndent(),
    )
  }

  @Test
  @RequiresD2
  fun `Choose custom layout engine`() = runScenario(D2CustomLayoutEngine) {
    // when we specify the "neato" layout engine
    val result = runTask("execD2Chart").build()

    // then
    assertThat(result.tasks).allSuccessful()
    assertThat(resolve("a/modular/chart.svg")).exists()
    assertThat(resolve("b/modular/chart.svg")).exists()
    assertThat(resolve("c/modular/chart.svg")).exists()
  }

  @Test
  @RequiresD2
  fun `Rerun when changing properties in the classes file`() = runScenario(D2CustomLayoutEngine) {
    // First run - all tasks run
    val result1 = runTask(":a:execD2Chart").build()
    assertThat(result1).taskHadResult(":writeD2Classes", SUCCESS)
    assertThat(result1).taskHadResult(":a:writeD2Chart", SUCCESS)
    assertThat(result1).taskHadResult(":a:execD2Chart", SUCCESS)

    // Second run with no changes - skipped
    val result2 = runTask(":a:execD2Chart").build()
    assertThat(result2).taskHadResult(":writeD2Classes", UP_TO_DATE)
    assertThat(result2).taskHadResult(":a:writeD2Chart", UP_TO_DATE)
    assertThat(result2).taskHadResult(":a:execD2Chart", UP_TO_DATE)

    // Third run setting a property to change the classes file - classes are written, chart is not but the output
    // file is regenerated
    val result3 = runTask(":a:execD2Chart", extras = listOf("-Pmodular.d2.theme=7")).build()
    assertThat(result3).taskHadResult(":writeD2Classes", SUCCESS)
    assertThat(result2).taskHadResult(":a:writeD2Chart", UP_TO_DATE)
    assertThat(result3).taskHadResult(":a:execD2Chart", SUCCESS)
  }
}
