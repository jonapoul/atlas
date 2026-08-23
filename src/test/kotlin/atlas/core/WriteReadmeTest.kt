package atlas.core

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.contentEquals
import atlas.test.doesNotExist
import atlas.test.exists
import atlas.test.resolve
import atlas.test.scenarios.MermaidBasic
import atlas.test.scenarios.MermaidWithLinkTypes
import blueprint.test.runTask
import blueprint.test.taskHadResult
import kotlin.test.Test

internal class WriteReadmeTest : ScenarioTest() {
  @Test
  fun `Mermaid readme links correctly with default outputs`() =
    runScenario(MermaidBasic) {
      // when
      val result = runTask("atlasGenerate").build()
      assertThat(result.tasks).allSuccessful()

      // then
      assertThat(resolve("a/README.md"))
        .exists()
        .contentEquals(
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
          """
            .trimIndent()
        )
    }

  @Test
  fun `Write mermaid readme with link types`() =
    runScenario(MermaidWithLinkTypes) {
      // when
      val result = runTask("atlasGenerate").build()
      assertThat(result.tasks).allSuccessful()

      // then
      assertThat(resolve("a/README.md"))
        .exists()
        .contentEquals(
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
          """
            .trimIndent()
        )
    }

  @Test
  fun `Inject mermaid into existing readme`() =
    runScenario(MermaidWithLinkTypes) {
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
        """
          .trimIndent()
      )

      // when
      val result = runTask(":a:writeReadme").build()
      assertThat(result.tasks).allSuccessful()

      // then
      val expected =
        """
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
        """
          .trimIndent()
      assertThat(readme).contentEquals(expected)

      // when we run again and force the regeneration
      val result2 = runTask(":a:writeReadme", "--rerun-tasks").build()
      assertThat(result2).taskHadResult(":a:writeReadme", SUCCESS)
      assertThat(readme).contentEquals(expected)
    }
}
