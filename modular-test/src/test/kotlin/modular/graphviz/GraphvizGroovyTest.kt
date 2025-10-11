/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import assertk.assertThat
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.GroovyGraphVizBasic
import modular.test.scenarios.GroovyGraphVizFull
import modular.test.scenarios.GroovyGraphVizModuleTypes
import modular.test.taskWasSuccessful
import org.junit.jupiter.api.Test

internal class GraphvizGroovyTest : ScenarioTest() {
  @Test
  @RequiresGraphviz
  internal fun `Configure graphviz`() = runScenario(GroovyGraphVizBasic) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }

  @Test
  @RequiresGraphviz
  internal fun `Configure graphviz module types`() = runScenario(GroovyGraphVizModuleTypes) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }

  @Test
  @RequiresGraphviz
  internal fun `Configure graphviz with everything`() = runScenario(GroovyGraphVizFull) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }
}
