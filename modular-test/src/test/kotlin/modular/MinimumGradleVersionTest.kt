/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular

import assertk.assertThat
import modular.test.MINIMUM_GRADLE_VERSION
import modular.test.Scenario
import modular.test.ScenarioTest
import modular.test.allTasksSuccessful
import modular.test.runTask
import modular.test.scenarios.D2AllModuleTypes
import modular.test.scenarios.GraphVizChartCustomConfig
import modular.test.scenarios.MermaidWithModuleTypes
import kotlin.test.Test

class MinimumGradleVersionTest : ScenarioTest() {
  @Test fun d2() = check(D2AllModuleTypes)
  @Test fun mermaid() = check(MermaidWithModuleTypes)
  @Test fun graphviz() = check(GraphVizChartCustomConfig)

  private fun check(scenario: Scenario) = runScenario(scenario) {
    // when
    val result = runTask("modularGenerate", gradleVersion = MINIMUM_GRADLE_VERSION).build()

    // then
    assertThat(result).allTasksSuccessful()
  }
}
