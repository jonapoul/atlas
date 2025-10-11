/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import assertk.assertThat
import assertk.assertions.exists
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.equalsDiffed
import modular.test.runTask
import modular.test.scenarios.MermaidWithGroupsNested
import modular.test.scenarios.MermaidWithGroupsNotNested
import modular.test.scenarios.MermaidWithoutGroups
import kotlin.test.Test

internal class WriteMermaidChartTest : ScenarioTest() {
  @Test
  internal fun `Write chart without groups`() = runScenario(MermaidWithoutGroups) {
    // when
    val result = runTask(":a:writeMermaidChart").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val chart = resolve("a/modular/chart.mmd")
    assertThat(chart).exists()
    assertThat(chart.readText()).equalsDiffed(
      """
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          _a --> _b
          _a --> _c
      """.trimIndent(),
    )
  }

  @Test
  internal fun `Write chart with groups enabled but no nested modules`() = runScenario(MermaidWithGroupsNotNested) {
    // when
    val result = runTask(":a:writeMermaidChart").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val chart = resolve("a/modular/chart.mmd")
    assertThat(chart).exists()
    assertThat(chart.readText()).equalsDiffed(
      """
        graph TD
          _a[":a"]
          _b[":b"]
          _c[":c"]
          _a --> _b
          _a --> _c
      """.trimIndent(),
    )
  }

  @Test
  internal fun `Write chart with groups enabled and modules nested`() = runScenario(MermaidWithGroupsNested) {
    // when
    val result = runTask(":a:writeMermaidChart").build()

    // then
    assertThat(result.tasks).allSuccessful()
    val chart = resolve("a/modular/chart.mmd")
    assertThat(chart).exists()
    assertThat(chart.readText()).equalsDiffed(
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
      """.trimIndent(),
    )
  }
}
