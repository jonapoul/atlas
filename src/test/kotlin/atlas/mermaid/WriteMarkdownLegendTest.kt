package atlas.mermaid

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.contentContains
import atlas.test.contentEquals
import atlas.test.exists
import atlas.test.resolve
import atlas.test.scenarios.MermaidWithLinkTypes
import atlas.test.scenarios.MermaidWithProjectTypes
import blueprint.test.runTask
import kotlin.test.Test

internal class WriteMarkdownLegendTest : ScenarioTest() {
  @Test
  fun `Write markdown legend with no link types`() =
    runScenario(MermaidWithProjectTypes) {
      // when
      val result = runTask(":writeMermaidLegend").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("atlas/mermaid/legend.md"))
        .exists()
        .contentContains(
          """
          | Project Types | Color |
          |:--:|:--:|
          | Kotlin JVM | <img src="https://img.shields.io/badge/-%20-mediumorchid?style=flat-square" height="30" width="100"> |
          | Java | <img src="https://img.shields.io/badge/-%20-orange?style=flat-square" height="30" width="100"> |
          """
            .trimIndent()
        )
    }

  @Test
  fun `Write markdown legend with no project types`() =
    runScenario(MermaidWithLinkTypes) {
      // when
      val result = runTask(":writeMermaidLegend").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("atlas/mermaid/legend.md"))
        .exists()
        .contentEquals(
          """
          | Link Types | Style |
          |:--:|:--:|
          | api | Green |
          | implementation | #5555FF |
          | compileOnly | Yellow Dashed |
          """
            .trimIndent()
        )
    }
}
