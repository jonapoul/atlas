package atlas.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import atlas.test.ScenarioTest
import atlas.test.buildRunner
import atlas.test.scenarios.ProjectTypeCreated
import atlas.test.scenarios.ProjectTypeRegistered
import atlas.test.scenarios.ProjectTypeWithNoIdentifiers
import kotlin.test.Test

internal class PluginValidationTest : ScenarioTest() {
  @Test
  fun `Warn if project type is declared with no identifiers`() = runScenario(ProjectTypeWithNoIdentifiers) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).contains(
      "Warning: Project type 'custom' will be ignored - you need to set one of " +
        "pathContains, pathMatches or hasPluginId.",
    )
  }

  @Test
  fun `Don't warn if project type is created`() = runScenario(ProjectTypeCreated) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail or log any warnings
    assertThat(result.output).doesNotContain("Warning")
  }

  @Test
  fun `Don't warn if project type is registered`() = runScenario(ProjectTypeRegistered) {
    // when we're not running any of our tasks
    val result = buildRunner()
      .withArguments("help")
      .build()

    // then the build didn't fail, but we get a log warning
    assertThat(result.output).doesNotContain("Warning")
  }
}
