package modular

import assertk.assertThat
import assertk.assertions.contains
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.MultiplePluginsApplied
import kotlin.test.Test

class MultiplePluginsTest : ScenarioTest() {
  @Test
  fun `Fail when multiple modular plugins are applied`() = runScenario(MultiplePluginsApplied) {
    // when
    val result = runTask("modularGenerate").buildAndFail()

    // then
    assertThat(result.output).contains(
      """
        > Cannot add extension with name 'modular', as there is an extension already registered with that name.
      """.trimIndent(),
    )
  }
}
