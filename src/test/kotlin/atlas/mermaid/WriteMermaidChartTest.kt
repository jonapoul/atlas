package atlas.mermaid

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.contentEquals
import atlas.test.exists
import atlas.test.resolve
import atlas.test.scenarios.MermaidWithGroupsNested
import atlas.test.scenarios.MermaidWithGroupsNotNested
import atlas.test.scenarios.MermaidWithoutGroups
import blueprint.test.runTask
import kotlin.test.Test

internal class WriteMermaidChartTest : ScenarioTest() {
  @Test
  fun `Write chart without groups`() =
    runScenario(MermaidWithoutGroups) {
      // when
      val result = runTask(":a:writeMermaidChart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/mermaid/chart.mmd"))
        .exists()
        .contentEquals(
          """
          graph TD
            _a[":a"]
            _b[":b"]
            _c[":c"]
            _a --> _b
            _a --> _c
          """
            .trimIndent()
        )
    }

  @Test
  fun `Write chart with groups enabled but no nested projects`() =
    runScenario(MermaidWithGroupsNotNested) {
      // when
      val result = runTask(":a:writeMermaidChart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/mermaid/chart.mmd"))
        .exists()
        .contentEquals(
          """
          graph TD
            _a[":a"]
            _b[":b"]
            _c[":c"]
            _a --> _b
            _a --> _c
          """
            .trimIndent()
        )
    }

  @Test
  fun `Write chart with groups enabled and projects nested`() =
    runScenario(MermaidWithGroupsNested) {
      // when
      val result = runTask(":a:writeMermaidChart").build()

      // then
      assertThat(result.tasks).allSuccessful()
      assertThat(resolve("a/atlas/mermaid/chart.mmd"))
        .exists()
        .contentEquals(
          """
          graph TD
            _a[":a"]
            subgraph b[":b"]
              _b_b1[":b:b1"]
              _b_b2[":b:b2"]
            end
            subgraph c[":c"]
              _c_c3[":c:c3"]
              subgraph inner[":inner"]
                _c_inner_c1[":c:inner:c1"]
                _c_inner_c2[":c:inner:c2"]
              end
            end
            _a --> _b_b1
            _a --> _b_b2
            _b_b1 --> _c_inner_c1
            _b_b1 --> _c_inner_c2
            _b_b2 --> _c_c3
            _b_b2 --> _c_inner_c2
          """
            .trimIndent()
        )
    }
}
