package atlas.graphviz.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import atlas.graphviz.RequiresGraphviz
import atlas.test.RequiresLn
import atlas.test.RequiresWhereis
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.doesNotExist
import atlas.test.runTask
import atlas.test.scenarios.GraphVizBasicWithPngOutput
import atlas.test.scenarios.GraphVizCustomDotExecutable
import atlas.test.scenarios.GraphVizCustomLayoutEngine
import atlas.test.scenarios.GraphvizBasic
import org.gradle.testkit.runner.TaskOutcome
import kotlin.test.Test

internal class ExecGraphvizTest : ScenarioTest() {
  @Test
  fun `No extras are generated if no file formats have been declared`() = runScenario(GraphvizBasic) {
    // when
    val result = runTask("atlasGenerate", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :a:writeModuleType SKIPPED
        :b:writeModuleType SKIPPED
        :c:writeModuleType SKIPPED
        :collateModuleTypes SKIPPED
        :a:writeModuleLinks SKIPPED
        :b:writeModuleLinks SKIPPED
        :c:writeModuleLinks SKIPPED
        :collateModuleLinks SKIPPED
        :a:writeModuleTree SKIPPED
        :a:writeGraphvizChart SKIPPED
        :a:execGraphvizChart SKIPPED
        :writeGraphvizLegend SKIPPED
        :execGraphvizLegend SKIPPED
        :a:writeGraphvizReadme SKIPPED
        :a:atlasGenerate SKIPPED
        :b:writeModuleTree SKIPPED
        :b:writeGraphvizChart SKIPPED
        :b:execGraphvizChart SKIPPED
        :b:writeGraphvizReadme SKIPPED
        :b:atlasGenerate SKIPPED
        :c:writeModuleTree SKIPPED
        :c:writeGraphvizChart SKIPPED
        :c:execGraphvizChart SKIPPED
        :c:writeGraphvizReadme SKIPPED
        :c:atlasGenerate SKIPPED

        BUILD SUCCESSFUL
      """.trimIndent(),
    )
  }

  @Test
  @RequiresGraphviz
  fun `Generate png file`() = runScenario(GraphVizBasicWithPngOutput) {
    // when
    val result = runTask("atlasGenerate").build()

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
      assertThat(resolve("$submodule/atlas/chart.png")).exists()
    }
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
    val result = runTask("atlasGenerate").build()

    // then it's all good
    assertThat(result.tasks).allSuccessful()

    // if we don't add this we'll get a junit log warning
    customDotFile.delete()
  }

  @Test
  @RequiresGraphviz
  fun `Fail with nonexistent custom path to dot command`() = runScenario(GraphVizCustomDotExecutable) {
    // Given we've made a symbolic link to a dot executable which doesn't exist
    assertThat(resolve("path/to/custom/dot")).doesNotExist()

    // when
    val result = runTask("atlasGenerate").buildAndFail()

    // then it fails as expected
    assertThat(result.output).containsMatch(
      """
        > A problem occurred starting process 'command '.*?/path/to/custom/dot''
      """.trimIndent().toRegex(),
    )
  }
}
