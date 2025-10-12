/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2.tasks

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.contentEquals
import atlas.test.runTask
import atlas.test.scenarios.D2Basic
import kotlin.test.Test

internal class WriteD2ChartTest : ScenarioTest() {
  @Test
  fun `Generate charts from basic config`() = runScenario(D2Basic) {
    // when
    val result = runTask("writeD2Chart").build()

    // then
    assertThat(result).allTasksSuccessful()

    // and the files were generated
    val d2FileA = resolve("a/atlas/chart.d2")
    val d2FileB = resolve("b/atlas/chart.d2")
    val d2FileC = resolve("c/atlas/chart.d2")

    // and contain expected contents, with modules in declaration order
    assertThat(d2FileA).contentEquals(
      """
        ...@../../atlas/classes.d2
        a: :a { class: module-KotlinJVM }
        b: :b { class: module-Java }
        c: :c { class: module-Java }
        a -> b
        a -> c
        vars: {
          d2-legend: {
            module-KotlinJVM: Kotlin JVM { class: module-KotlinJVM }
            module-Java: Java { class: module-Java }
          }
        }
      """.trimIndent(),
    )

    assertThat(d2FileB).contentEquals(
      """
        ...@../../atlas/classes.d2
        b: :b { class: module-Java }
        vars: {
          d2-legend: {
            module-Java: Java { class: module-Java }
          }
        }
      """.trimIndent(),
    )

    assertThat(d2FileC).contentEquals(
      """
        ...@../../atlas/classes.d2
        c: :c { class: module-Java }
        vars: {
          d2-legend: {
            module-Java: Java { class: module-Java }
          }
        }
      """.trimIndent(),
    )
  }
}
