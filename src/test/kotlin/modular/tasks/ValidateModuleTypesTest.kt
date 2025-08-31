package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import modular.test.ModularTest
import modular.test.buildRunner
import modular.test.scenarios.ModuleTypeWithNoIdentifiers
import modular.test.scenarios.NoModuleTypesDeclared
import kotlin.test.Test

class ValidateModuleTypesTest : ModularTest() {
  @Test
  fun `Warn if module type is declared with no identifiers`() = runScenario(ModuleTypeWithNoIdentifiers) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).contains(
      "Warning: Module type 'custom' will be ignored - you need to set one of " +
        "pathContains, pathMatches or hasPluginId."
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
}
