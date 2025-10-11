/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.runTask
import modular.test.scenarios.MermaidWithLinkTypes
import modular.test.scenarios.MermaidWithModuleTypes
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

internal class WriteMarkdownLegendTest : ScenarioTest() {
  @Test
  fun `Write markdown legend with no link types`() = runScenario(MermaidWithModuleTypes) {
    // when
    val result = runTask(":writeMermaidLegend").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val legend = resolve("modular/legend.md")
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

  @Disabled("https://github.com/jonapoul/modular/issues/247")
  @Test
  fun `Write markdown legend with no module types`() = runScenario(MermaidWithLinkTypes) {
    // when
    val result = runTask(":writeMermaidLegend").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val legend = resolve("modular/legend.md")
    assertThat(legend).exists()
    assertThat(legend.readText()).contains(
      """
        | Link Types | Style |
        |:--:|:--:|
        | api | Green |
        | implementation | #5555FF |
        | compileOnly | Yellow Dotted |
      """.trimIndent(),
    )
  }
}
