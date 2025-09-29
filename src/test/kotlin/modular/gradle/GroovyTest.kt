/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import assertk.assertThat
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.GroovyGraphVizBasic
import modular.test.scenarios.GroovyGraphVizFull
import modular.test.scenarios.GroovyGraphVizModuleTypes
import modular.test.taskWasSuccessful
import kotlin.test.Test

class GroovyTest : ScenarioTest() {
  @Test
  fun `Configure graphviz`() = runScenario(GroovyGraphVizBasic) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }

  @Test
  fun `Configure graphviz module types`() = runScenario(GroovyGraphVizModuleTypes) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }

  @Test
  fun `Configure graphviz with everything`() = runScenario(GroovyGraphVizFull) {
    // when
    val result = runTask("modularGenerate").build()

    // then
    assertThat(result).taskWasSuccessful(":modularGenerate")
  }
}
