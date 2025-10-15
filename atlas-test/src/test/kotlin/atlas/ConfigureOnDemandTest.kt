/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas

import assertk.assertThat
import assertk.assertions.exists
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.noTasksFailed
import atlas.test.runTask
import atlas.test.scenarios.D2ConfigureOnDemand
import kotlin.test.Test

internal class ConfigureOnDemandTest : ScenarioTest() {
  @Test
  fun `Support Gradle configureOnDemand flag`() = runScenario(D2ConfigureOnDemand) {
    // when we generate
    val generate = runTask("atlasGenerate").build()

    // then
    assertThat(generate).allTasksSuccessful()
    assertThat(resolve("a/atlas/chart.d2")).exists()
    assertThat(resolve("a/atlas/chart.svg")).exists()

    // when we check
    val check = runTask("atlasCheck").build()

    // then
    assertThat(check).noTasksFailed()
  }
}
