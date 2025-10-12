/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import assertk.assertThat
import modular.test.ScenarioTest
import modular.test.allTasksSuccessful
import modular.test.contentEquals
import modular.test.runTask
import modular.test.scenarios.D2Basic
import kotlin.test.Test

internal class WriteD2ChartTest : ScenarioTest() {
  @Test
  fun `Generate charts from basic config`() = runScenario(D2Basic) {
    // when
    val result = runTask("writeD2Chart").build()

    // then
    assertThat(result).allTasksSuccessful()

    // and the files were generated
    val d2FileA = resolve("a/modular/chart.d2")
    val d2FileB = resolve("b/modular/chart.d2")
    val d2FileC = resolve("c/modular/chart.d2")

    // and contain expected contents, with modules in declaration order
    assertThat(d2FileA).contentEquals(
      """
        ...@../../modular/classes.d2
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
        ...@../../modular/classes.d2
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
        ...@../../modular/classes.d2
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
