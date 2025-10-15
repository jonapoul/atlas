/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.noTasksFailed
import atlas.test.runTask
import atlas.test.scenarios.D2ConfigureOnDemand
import atlas.test.scenarios.GraphvizConfigureOnDemand
import atlas.test.scenarios.MermaidConfigureOnDemand
import java.io.File
import kotlin.test.Test

internal class ConfigureOnDemandTest : ScenarioTest() {
  @Test fun `D2 configureOnDemand`() = runScenario(D2ConfigureOnDemand) { testCase() }
  @Test fun `Graphviz configureOnDemand`() = runScenario(GraphvizConfigureOnDemand) { testCase() }
  @Test fun `Mermaid configureOnDemand`() = runScenario(MermaidConfigureOnDemand) { testCase() }

  private fun File.testCase() {
    val generate = runTask("atlasGenerate").build()
    assertThat(generate).allTasksSuccessful()

    val check = runTask("atlasCheck").build()
    assertThat(check).noTasksFailed()
  }
}
