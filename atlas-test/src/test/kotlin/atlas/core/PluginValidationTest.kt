/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import assertk.assertThat
import assertk.assertions.contains
import atlas.test.ScenarioTest
import atlas.test.buildRunner
import atlas.test.scenarios.ModuleTypeWithNoIdentifiers
import kotlin.test.Test

internal class PluginValidationTest : ScenarioTest() {
  @Test
  fun `Warn if module type is declared with no identifiers`() = runScenario(ModuleTypeWithNoIdentifiers) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).contains(
      "Warning: Module type 'custom' will be ignored - you need to set one of " +
        "pathContains, pathMatches or hasPluginId.",
    )
  }
}
