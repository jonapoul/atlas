/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import modular.test.ScenarioTest
import modular.test.buildRunner
import modular.test.scenarios.DotFileBasic
import modular.test.scenarios.DotFileBigGraph100DpiSvg
import modular.test.scenarios.InvalidColorDeclaration
import modular.test.scenarios.ModuleTypeWithNoIdentifiers
import modular.test.scenarios.NoModuleTypesDeclared
import org.codehaus.groovy.syntax.Types.REGEX_PATTERN
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
  fun `Warn if no module types registered`() = runScenario(NoModuleTypesDeclared) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).contains("Warning: No module types have been registered!")
  }

  @Test
  fun `Fail if module type color doesn't match pattern`() = runScenario(InvalidColorDeclaration) {
    val result = buildRunner()
      .withArguments("help")
      .buildAndFail()

    // then the build failed
    assertThat(result.output).contains(
      "Invalid color string 'ABCXYZ' - should match regex pattern '$REGEX_PATTERN'",
    )
  }

  @Test
  fun `Warn if custom DPI and SVG configured together`() = runScenario(DotFileBigGraph100DpiSvg) {
    // when an irrelevant task is run (AKA gradle is initialised)
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then a warning was printed to enable the experimental property
    val msg = "Configuring a custom DPI on a dotfile's with SVG output enabled will likely cause a misaligned viewBox"
    assertThat(result.output).contains(msg)

    // when we run again with the suppress property enabled
    val suppressedResult = buildRunner()
      .withArguments("help", "-Pmodular.suppress.adjustSvgViewBox=true")
      .build()

    // Then the warning wasn't printed
    assertThat(suppressedResult.output).doesNotContain(msg)
  }

  @Test
  fun `Warn if GraphViz config has no file outputs configured`() = runScenario(DotFileBasic) {
    // when an irrelevant task is run (AKA gradle is initialised)
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then a warning was printed to enable the experimental property
    val expectedWarning = "No file formats have been registered as GraphViz outputs"
    assertThat(result.output).contains(expectedWarning)

    // when we run again with the suppress property enabled
    val suppressedResult = buildRunner()
      .withArguments("help", "-Pmodular.suppress.noGraphVizOutputs=true")
      .build()

    // Then the warning wasn't printed
    assertThat(suppressedResult.output).doesNotContain(expectedWarning)
  }
}
