/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import assertk.assertThat
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.doesNotExist
import modular.test.equalsDiffed
import modular.test.runTask
import modular.test.scenarios.MermaidBasic
import modular.test.scenarios.MermaidWithLinkTypes
import modular.test.taskHadResult
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import kotlin.test.Test

internal class WriteReadmeTest : ScenarioTest() {
  @Test
  fun `Mermaid readme links correctly with default outputs`() = runScenario(MermaidBasic) {
    // when
    val result = runTask("modularGenerate").build()
    assertThat(result.tasks).allSuccessful()

    // then
    val readme = resolve("a/README.md")
    assertThat(readme).exists()
    assertThat(readme.readText()).equalsDiffed(
      """
        # a

        <!--region chart-->
        ```mermaid
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          _a --> _b
          _a --> _c
        ```
        <!--endregion-->
      """.trimIndent(),
    )
  }

  @Test
  fun `Write mermaid readme with link types`() = runScenario(MermaidWithLinkTypes) {
    // when
    val result = runTask("modularGenerate").build()
    assertThat(result.tasks).allSuccessful()

    // then
    val readme = resolve("a/README.md")
    assertThat(readme).exists()
    assertThat(readme.readText()).equalsDiffed(
      """
        # a

        <!--region chart-->
        ```mermaid
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          _a --> _b
          linkStyle 0 stroke:green
          _a --> _c
          linkStyle 1 stroke:#5555FF
        ```

        | Link Types | Style |
        |:--:|:--:|
        | api | Green |
        | implementation | #5555FF |
        | compileOnly | Yellow Dashed |
        <!--endregion-->
      """.trimIndent(),
    )
  }

  @Test
  fun `Inject mermaid into existing readme`() = runScenario(MermaidWithLinkTypes) {
    // given
    val readme = resolve("a/README.md")
    assertThat(readme).doesNotExist()
    readme.writeText(
      """
        # My custom readme title

        Some prefix

        <!--region chart-->

        <!--endregion-->

        Some suffix
      """.trimIndent(),
    )

    // when
    val result = runTask(":a:writeMermaidReadme").build()
    assertThat(result.tasks).allSuccessful()

    // then
    val expected = """
      # My custom readme title

      Some prefix

      <!--region chart-->
      ```mermaid
      graph TD
        _a[":a"]
        _b[":b"]
        _c[":c"]
        _a --> _b
        linkStyle 0 stroke:green
        _a --> _c
        linkStyle 1 stroke:#5555FF
      ```

      | Link Types | Style |
      |:--:|:--:|
      | api | Green |
      | implementation | #5555FF |
      | compileOnly | Yellow Dashed |
      <!--endregion-->

      Some suffix
    """.trimIndent()
    assertThat(readme.readText()).equalsDiffed(expected)

    // when we run again and force the regeneration
    val result2 = runTask(":a:writeMermaidReadme", extras = listOf("--rerun-tasks")).build()
    assertThat(result2).taskHadResult(":a:writeMermaidReadme", SUCCESS)
    assertThat(readme.readText()).equalsDiffed(expected)
  }
}
