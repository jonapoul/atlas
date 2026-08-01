package atlas

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import atlas.test.ScenarioTest
import atlas.test.noTasksFailed
import atlas.test.runTask
import atlas.test.scenarios.MultipleFrameworks
import kotlin.test.Test

internal class MultipleFrameworksTest : ScenarioTest() {
  @Test
  fun `Generate charts for every configured framework`() =
    runScenario(MultipleFrameworks) {
      // when
      val result = runTask("atlasGenerate").build()
      assertThat(result).noTasksFailed()

      // then each framework wrote to its own directory, so nothing was overwritten
      assertThat(resolve("a/atlas/d2/chart.d2")).exists()
      assertThat(resolve("a/atlas/graphviz/chart.dot")).exists()
      assertThat(resolve("a/atlas/mermaid/chart.mmd")).exists()

      // and the shared legends live in the root project
      assertThat(resolve("atlas/d2/classes.d2")).exists()
      assertThat(resolve("atlas/graphviz/legend.dot")).exists()
      assertThat(resolve("atlas/mermaid/legend.md")).exists()
    }

  @Test
  fun `Write every framework's diagram into a single readme`() =
    runScenario(MultipleFrameworks) {
      // when
      runTask("atlasGenerate").build()

      // then
      val readme = resolve("a/README.md").readText()
      assertThat(readme).contains("![chart](atlas/d2/chart.svg)")
      assertThat(readme).contains("![chart](atlas/graphviz/chart.svg)")
      assertThat(readme).contains("```mermaid")
    }
}
