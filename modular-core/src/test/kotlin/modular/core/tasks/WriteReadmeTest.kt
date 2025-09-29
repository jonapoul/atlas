/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import assertk.assertThat
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.containsDiffed
import modular.test.doesNotExist
import modular.test.runTask
import modular.test.scenarios.MermaidBasic
import modular.test.scenarios.MermaidWithCustomOutputs
import modular.test.scenarios.MermaidWithLinkTypes
import modular.test.taskHadResult
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import kotlin.test.Test

class WriteReadmeTest : ScenarioTest() {
  @Test
  fun `Mermaid readme links correctly with default outputs`() = runScenario(MermaidBasic) {
    // when
    val result = runTask("modularGenerate").build()
    assertThat(result.tasks).allSuccessful()

    // then
    val readme = resolve("a/README.md")
    assertThat(readme).exists()
    assertThat(readme.readText()).containsDiffed(
      """
        # a

        <!--region chart-->

        ```mermaid
        ---
        config:
        ---
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          style _a color:black,font-weight:bold,stroke:black,stroke-width:2px
          style _b color:black
          style _c color:black
          _a --> _b
          _a --> _c
        ```

        <!--endregion-->
      """.trimIndent(),
    )
  }

  @Test
  fun `Mermaid readme links correctly with custom outputs`() = runScenario(MermaidWithCustomOutputs) {
    // when
    val result = runTask("modularGenerate").build()
    assertThat(result.tasks).allSuccessful()

    // then
    val readme = resolve("a/README.md")
    assertThat(readme).exists()
    assertThat(readme.readText()).containsDiffed(
      """
        # a

        <!--region chart-->

        ```mermaid
        ---
        config:
        ---
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          style _a color:black,font-weight:bold,stroke:black,stroke-width:2px
          style _b color:black
          style _c color:black
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
    assertThat(readme.readText()).containsDiffed(
      """
        # a

        <!--region chart-->

        ```mermaid
        ---
        config:
        ---
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          style _a color:black,font-weight:bold,stroke:black,stroke-width:2px
          style _b color:black
          style _c color:black
          _a --> _b
          _a --> _c
        ```

        | Link Types | Style |
        |:--:|:--:|
        | api | Green |
        | implementation | #5555FF |
        | compileOnly | Yellow Dotted |

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
      ---
      config:
      ---
      graph TD
        _a[":a"]
        _b[":b"]
        _c[":c"]
        style _a color:black,font-weight:bold,stroke:black,stroke-width:2px
        style _b color:black
        style _c color:black
        _a --> _b
        _a --> _c
      ```

      | Link Types | Style |
      |:--:|:--:|
      | api | Green |
      | implementation | #5555FF |
      | compileOnly | Yellow Dotted |

      <!--endregion-->

      Some suffix
    """.trimIndent()
    assertThat(readme.readText()).containsDiffed(expected)

    // when we run again and force the regeneration
    val result2 = runTask(":a:writeMermaidReadme", extras = listOf("--rerun-tasks")).build()
    assertThat(result2).taskHadResult(":a:writeMermaidReadme", SUCCESS)
    assertThat(readme.readText()).containsDiffed(expected)
  }
}
