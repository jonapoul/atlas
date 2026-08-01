package atlas.graphviz

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.scenarios.GroovyGraphVizBasic
import atlas.test.scenarios.GroovyGraphVizFull
import atlas.test.scenarios.GroovyGraphVizProjectTypes
import blueprint.test.runTask
import blueprint.test.taskSucceeded
import org.junit.jupiter.api.Test

internal class GraphvizGroovyTest : ScenarioTest() {
  @Test
  @RequiresGraphviz
  fun `Configure graphviz`() =
    runScenario(GroovyGraphVizBasic) {
      // when
      val result = runTask("atlasGenerate").build()

      // then
      assertThat(result)
        .taskSucceeded(":a:atlasGenerate")
        .taskSucceeded(":b:atlasGenerate")
        .taskSucceeded(":c:atlasGenerate")
    }

  @Test
  @RequiresGraphviz
  fun `Configure graphviz project types`() =
    runScenario(GroovyGraphVizProjectTypes) {
      // when
      val result = runTask("atlasGenerate").build()

      // then
      assertThat(result)
        .taskSucceeded(":a:atlasGenerate")
        .taskSucceeded(":b:atlasGenerate")
        .taskSucceeded(":c:atlasGenerate")
    }

  @Test
  @RequiresGraphviz
  fun `Configure graphviz with everything`() =
    runScenario(GroovyGraphVizFull) {
      // when
      val result = runTask("atlasGenerate").build()

      // then
      assertThat(result)
        .taskSucceeded(":a:atlasGenerate")
        .taskSucceeded(":b:atlasGenerate")
        .taskSucceeded(":c:atlasGenerate")
    }
}
