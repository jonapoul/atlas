package atlas

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.childExists
import atlas.test.contentContains
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

      // then each framework wrote to its own directory, so nothing was overwritten, and the shared
      // legends live in the root project
      assertThat(this)
        .childExists("a/atlas/d2/chart.d2")
        .childExists("a/atlas/graphviz/chart.dot")
        .childExists("a/atlas/mermaid/chart.mmd")
        .childExists("atlas/d2/classes.d2")
        .childExists("atlas/graphviz/legend.dot")
        .childExists("atlas/mermaid/legend.md")
    }

  @Test
  fun `Write every framework's diagram into a single readme`() =
    runScenario(MultipleFrameworks) {
      // when
      runTask("atlasGenerate").build()

      // then
      assertThat(resolve("a/README.md"))
        .contentContains("![chart](atlas/d2/chart.svg)")
        .contentContains("![chart](atlas/graphviz/chart.svg)")
        .contentContains("```mermaid")
    }
}
