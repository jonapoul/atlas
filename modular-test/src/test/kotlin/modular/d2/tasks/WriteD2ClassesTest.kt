/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import assertk.assertThat
import modular.test.ScenarioTest
import modular.test.equalsDiffed
import modular.test.runTask
import modular.test.scenarios.D2AllModuleTypes
import modular.test.taskWasSuccessful
import kotlin.test.Test

class WriteD2ClassesTest : ScenarioTest() {
  @Test
  fun `Generate classes from all default types`() = runScenario(D2AllModuleTypes) {
    // when
    val result = runTask("writeD2Classes").build()

    // then
    assertThat(result).taskWasSuccessful(":writeD2Classes")

    // and the file was generated
    assertThat(resolve("modular/classes.d2").readText()).equalsDiffed(
      """
        classes: {
          module-AndroidApp {
            style.fill: "limegreen"
          }
          module-KotlinMultiplatform {
            style.fill: "mediumslateblue"
          }
          module-AndroidLibrary {
            style.fill: "lightgreen"
          }
          module-KotlinJVM {
            style.fill: "mediumorchid"
          }
          module-Java {
            style.fill: "orange"
          }
          module-Other {
            style.fill: "gainsboro"
          }
          container {
          }
        }
      """.trimIndent(),
    )
  }
}
