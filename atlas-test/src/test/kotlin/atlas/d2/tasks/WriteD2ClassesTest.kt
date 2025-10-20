package atlas.d2.tasks

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.equalsDiffed
import atlas.test.runTask
import atlas.test.scenarios.D2AllModuleTypes
import atlas.test.taskWasSuccessful
import kotlin.test.Test

internal class WriteD2ClassesTest : ScenarioTest() {
  @Test
  fun `Generate classes from all default types`() = runScenario(D2AllModuleTypes) {
    // when
    val result = runTask("writeD2Classes").build()

    // then
    assertThat(result).taskWasSuccessful(":writeD2Classes")

    // and the file was generated
    assertThat(resolve("atlas/classes.d2").readText()).equalsDiffed(
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
          hidden {
            style.opacity: 0
          }
        }
      """.trimIndent(),
    )
  }
}
