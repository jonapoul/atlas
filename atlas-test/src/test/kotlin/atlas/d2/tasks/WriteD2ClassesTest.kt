package atlas.d2.tasks

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.equalsDiffed
import atlas.test.runTask
import atlas.test.scenarios.D2AllProjectTypes
import atlas.test.taskWasSuccessful
import kotlin.test.Test

internal class WriteD2ClassesTest : ScenarioTest() {
  @Test
  fun `Generate classes from all default types`() = runScenario(D2AllProjectTypes) {
    // when
    val result = runTask("writeD2Classes").build()

    // then
    assertThat(result).taskWasSuccessful(":writeD2Classes")

    // and the file was generated
    assertThat(resolve("atlas/classes.d2").readText()).equalsDiffed(
      """
        classes: {
          project-AndroidApp {
            style.fill: "limegreen"
          }
          project-KotlinMultiplatform {
            style.fill: "mediumslateblue"
          }
          project-AndroidLibrary {
            style.fill: "lightgreen"
          }
          project-KotlinJVM {
            style.fill: "mediumorchid"
          }
          project-Java {
            style.fill: "orange"
          }
          project-Other {
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
