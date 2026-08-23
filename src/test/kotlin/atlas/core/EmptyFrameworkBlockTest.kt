package atlas.core

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.exists
import atlas.test.resolve
import atlas.test.scenarios.GroovyD2Basic
import atlas.test.scenarios.GroovyGraphVizBasic
import atlas.test.scenarios.GroovyMermaidBasic
import blueprint.test.runTask
import kotlin.test.Test

/**
 * Groovy settings scripts can't use Kotlin default parameter values, so they need a real zero-arg
 * overload of `d2()`/`graphviz()`/`mermaid()` to call. These scenarios have no `atlasConfig` at
 * all, so they only exercise that bare call.
 */
internal class EmptyFrameworkBlockTest : ScenarioTest() {
  @Test
  fun `Bare d2() with no config registers D2 generation`() =
    runScenario(GroovyD2Basic) {
      // when
      val result = runTask("writeD2Chart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/d2/chart.d2")).exists()
    }

  @Test
  fun `Bare graphviz() with no config registers Graphviz generation`() =
    runScenario(GroovyGraphVizBasic) {
      // when
      val result = runTask("writeGraphvizChart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/graphviz/chart.dot")).exists()
    }

  @Test
  fun `Bare mermaid() with no config registers Mermaid generation`() =
    runScenario(GroovyMermaidBasic) {
      // when
      val result = runTask("writeMermaidChart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/mermaid/chart.mmd")).exists()
    }
}
