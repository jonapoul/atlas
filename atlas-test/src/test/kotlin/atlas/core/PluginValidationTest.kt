package atlas.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import atlas.test.ScenarioTest
import atlas.test.buildRunner
import atlas.test.scenarios.ModuleTypeCreated
import atlas.test.scenarios.ModuleTypeRegistered
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

  @Test
  fun `Don't warn if module type is created`() = runScenario(ModuleTypeCreated) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail or log any warnings
    assertThat(result.output).doesNotContain("Warning")
  }

  @Test
  fun `Don't warn if module type is registered`() = runScenario(ModuleTypeRegistered) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).doesNotContain("Warning")
  }
}
