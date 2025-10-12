/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.runTask
import atlas.test.scenarios.GroovyGraphVizBasic
import atlas.test.scenarios.GroovyGraphVizFull
import atlas.test.scenarios.GroovyGraphVizModuleTypes
import atlas.test.taskWasSuccessful
import org.junit.jupiter.api.Test

internal class GraphvizGroovyTest : ScenarioTest() {
  @Test
  @RequiresGraphviz
  fun `Configure graphviz`() = runScenario(GroovyGraphVizBasic) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":atlasGenerate")
  }

  @Test
  @RequiresGraphviz
  fun `Configure graphviz module types`() = runScenario(GroovyGraphVizModuleTypes) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":atlasGenerate")
  }

  @Test
  @RequiresGraphviz
  fun `Configure graphviz with everything`() = runScenario(GroovyGraphVizFull) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":atlasGenerate")
  }
}
