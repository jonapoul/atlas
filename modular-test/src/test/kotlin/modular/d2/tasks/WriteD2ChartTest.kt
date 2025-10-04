/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import assertk.assertThat
import modular.test.ScenarioTest
import modular.test.contentEquals
import modular.test.runTask
import modular.test.scenarios.D2Basic
import kotlin.test.Test

class WriteD2ChartTest : ScenarioTest() {
  @Test
  fun `Generate charts from basic config`() = runScenario(D2Basic) {
    // when
    runTask("writeD2Chart").build()

    // then the files were generated
    val d2FileA = resolve("a/modular/chart.d2")
    val d2FileB = resolve("b/modular/chart.d2")
    val d2FileC = resolve("c/modular/chart.d2")

    // and contain expected contents, with modules in declaration order
    assertThat(d2FileA).contentEquals(
      """
        a: :a
        b: :b
        c: :c
        a -> b
        a -> c
      """.trimIndent(),
    )

    assertThat(d2FileB).contentEquals("b: :b")
    assertThat(d2FileC).contentEquals("c: :c")
  }
}
