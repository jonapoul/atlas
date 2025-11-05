package atlas.d2.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import atlas.d2.RequiresD2
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.runTask
import atlas.test.scenarios.D2Basic
import atlas.test.scenarios.D2CustomLayoutEngine
import atlas.test.taskHadResult
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import kotlin.test.Test

internal class ExecD2Test : ScenarioTest() {
  @Test
  fun `No extras are generated if no file formats have been declared`() = runScenario(D2Basic) {
    // when
    val result = runTask("atlasGenerate", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :writeD2Classes SKIPPED
        :a:writeProjectType SKIPPED
        :b:writeProjectType SKIPPED
        :c:writeProjectType SKIPPED
        :collateProjectTypes SKIPPED
        :a:writeProjectLinks SKIPPED
        :b:writeProjectLinks SKIPPED
        :c:writeProjectLinks SKIPPED
        :collateProjectLinks SKIPPED
        :a:writeProjectTree SKIPPED
        :a:writeD2Chart SKIPPED
        :a:execD2Chart SKIPPED
        :a:writeD2Readme SKIPPED
        :a:atlasGenerate SKIPPED
        :b:writeProjectTree SKIPPED
        :b:writeD2Chart SKIPPED
        :b:execD2Chart SKIPPED
        :b:writeD2Readme SKIPPED
        :b:atlasGenerate SKIPPED
        :c:writeProjectTree SKIPPED
        :c:writeD2Chart SKIPPED
        :c:execD2Chart SKIPPED
        :c:writeD2Readme SKIPPED
        :c:atlasGenerate SKIPPED

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
    assertThat(resolve("a/atlas/chart.svg")).exists()
    assertThat(resolve("b/atlas/chart.svg")).exists()
    assertThat(resolve("c/atlas/chart.svg")).exists()
  }

  @Test
  @RequiresD2
  fun `Rerun when changing properties in the classes file`() = runScenario(D2CustomLayoutEngine) {
    // First run - all tasks run
    assertThat(runTask(":a:execD2Chart").build())
      .taskHadResult(":writeD2Classes", SUCCESS)
      .taskHadResult(":a:writeD2Chart", SUCCESS)
      .taskHadResult(":a:execD2Chart", SUCCESS)

    // Second run with no changes - skipped
    assertThat(runTask(":a:execD2Chart").build())
      .taskHadResult(":writeD2Classes", UP_TO_DATE)
      .taskHadResult(":a:writeD2Chart", UP_TO_DATE)
      .taskHadResult(":a:execD2Chart", UP_TO_DATE)

    // Third run setting a property to change the classes file - classes are written, chart is not but the output
    // file is regenerated
    val result3 = runTask(":a:execD2Chart", extras = listOf("-Patlas.d2.theme=7")).build()
    assertThat(result3)
      .taskHadResult(":writeD2Classes", SUCCESS)
      .taskHadResult(":a:writeD2Chart", UP_TO_DATE)
      .taskHadResult(":a:execD2Chart", SUCCESS)
  }
}
