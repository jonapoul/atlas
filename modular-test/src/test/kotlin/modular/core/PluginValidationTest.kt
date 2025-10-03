/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import modular.test.ScenarioTest
import modular.test.buildRunner
import modular.test.scenarios.GraphVizBigGraph100DpiSvg
import modular.test.scenarios.ModuleTypeWithNoIdentifiers
import kotlin.test.Test

class PluginValidationTest : ScenarioTest() {
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

  @Test
  fun `Warn if custom DPI and SVG configured together`() = runScenario(GraphVizBigGraph100DpiSvg) {
    // when an irrelevant task is run (AKA gradle is initialised)
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then a warning was printed to enable the experimental property
    val msg = "Configuring a custom DPI with SVG output enabled will likely cause a misaligned viewBox"
    assertThat(result.output).contains(msg)

    // when we run again with the suppress property enabled
    val suppressedResult = buildRunner()
      .withArguments("help", "-Pmodular.graphviz.suppressAdjustSvgViewBox=true")
      .build()

    // Then the warning wasn't printed
    assertThat(suppressedResult.output).doesNotContain(msg)
  }
}
