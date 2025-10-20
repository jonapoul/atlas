package atlas

import assertk.assertThat
import assertk.assertions.contains
import atlas.test.ScenarioTest
import atlas.test.runTask
import atlas.test.scenarios.MultiplePluginsApplied
import kotlin.test.Test

internal class MultiplePluginsTest : ScenarioTest() {
  @Test
  fun `Fail when multiple atlas plugins are applied`() = runScenario(MultiplePluginsApplied) {
    // when
    val result = runTask("atlasGenerate").buildAndFail()

    // then
    assertThat(result.output).contains(
      """
        > Cannot add extension with name 'atlas', as there is an extension already registered with that name.
      """.trimIndent(),
    )
  }
}
