package atlas.mermaid

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.equalsDiffed
import atlas.test.runTask
import atlas.test.scenarios.MermaidWithLinkTypes
import atlas.test.scenarios.MermaidWithModuleTypes
import kotlin.test.Test

internal class WriteMarkdownLegendTest : ScenarioTest() {
  @Test
  fun `Write markdown legend with no link types`() = runScenario(MermaidWithModuleTypes) {
    // when
    val result = runTask(":writeMermaidLegend").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val legend = resolve("atlas/legend.md")
    assertThat(legend).exists()
    assertThat(legend.readText()).contains(
      """
        | Module Types | Color |
        |:--:|:--:|
        | Kotlin JVM | <img src="https://img.shields.io/badge/-%20-mediumorchid?style=flat-square" height="30" width="100"> |
        | Java | <img src="https://img.shields.io/badge/-%20-orange?style=flat-square" height="30" width="100"> |
      """.trimIndent(),
    )
  }

  @Test
  fun `Write markdown legend with no module types`() = runScenario(MermaidWithLinkTypes) {
    // when
    val result = runTask(":writeMermaidLegend").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val legend = resolve("atlas/legend.md")
    assertThat(legend).exists()
    assertThat(legend.readText()).equalsDiffed(
      """
        | Link Types | Style |
        |:--:|:--:|
        | api | Green |
        | implementation | #5555FF |
        | compileOnly | Yellow Dashed |
      """.trimIndent(),
    )
  }
}
