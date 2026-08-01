package atlas.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import atlas.test.ScenarioTest
import atlas.test.buildRunner
import atlas.test.runTask
import atlas.test.scenarios.MermaidBasic
import atlas.test.scenarios.NoFrameworksConfigured
import atlas.test.scenarios.PropertiesForUnusedFrameworks
import atlas.test.scenarios.UnsupportedLinkStyle
import kotlin.test.Test

internal class FrameworkConfigTest : ScenarioTest() {
  @Test
  fun `Warn about style properties which no configured framework reads`() =
    runScenario(PropertiesForUnusedFrameworks) {
      // when
      val result = buildRunner().withArguments("help").build()

      // then the D2-only and Graphviz-only properties are called out, grouped by framework
      assertThat(result.output)
        .contains(
          "Warning: project type 'Kotlin JVM' sets render3D and shadow, which only D2 uses. " +
            "Configure the d2 { } block to use them, or remove the config."
        )
      assertThat(result.output)
        .contains(
          "Warning: project type 'Kotlin JVM' sets peripheries, which only Graphviz uses. " +
            "Configure the graphviz { } block to use it, or remove the config."
        )

      // but stroke is understood by Mermaid, so it isn't mentioned
      assertThat(result.output).doesNotContain("sets stroke")
    }

  @Test
  fun `Don't warn about properties the configured framework reads`() =
    runScenario(MermaidBasic) {
      // when
      val result = buildRunner().withArguments("help").build()

      // then
      assertThat(result.output).doesNotContain("Warning")
    }

  @Test
  fun `Warn when a link style isn't supported by a configured framework`() =
    runScenario(UnsupportedLinkStyle) {
      // when
      val result = buildRunner().withArguments("help").build()

      // then
      assertThat(result.output)
        .contains(
          "Warning: link type 'api' uses the dotted style, which Mermaid can't draw - " +
            "Atlas will fall back to the closest style it has."
        )
    }

  @Test
  fun `Warn when no frameworks are configured`() =
    runScenario(NoFrameworksConfigured) {
      // when
      val result = buildRunner().withArguments("help").build()

      // then
      assertThat(result.output)
        .contains(
          "Warning: no Atlas diagram frameworks are configured, so no charts will be generated."
        )
    }

  @Test
  fun `Only register tasks for configured frameworks`() =
    runScenario(MermaidBasic) {
      // when
      val result = runTask("tasks", extras = listOf("--all")).build()

      // then
      assertThat(result.output).contains("writeMermaidChart")
      assertThat(result.output).doesNotContain("writeGraphvizChart")
      assertThat(result.output).doesNotContain("writeD2Chart")
    }
}
