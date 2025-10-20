package atlas.d2.tasks

import assertk.assertThat
import assertk.assertions.exists
import atlas.d2.RequiresD2
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.contentEquals
import atlas.test.runTask
import atlas.test.scenarios.D2Basic
import atlas.test.scenarios.D2NestedModules
import atlas.test.taskWasSuccessful
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

  @Test
  @RequiresD2
  fun `Write correct classes file path for nested modules`() = runScenario(D2NestedModules) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(result).allTasksSuccessful()

    // and the file was generated
    val chartFile = resolve("path/to/my/module/atlas/chart.d2")
    assertThat(chartFile.exists())
    assertThat(resolve("atlas/classes.d2")).exists()
    assertThat(chartFile).contentEquals(
      """
        ...@../../../../../atlas/classes.d2
        path_to_my_module: :path:to:my:module
      """.trimIndent(),
    )

    // when we check
    val checkResult = runTask("check").build()

    // then
    assertThat(checkResult).taskWasSuccessful(":path:to:my:module:checkD2Chart")
  }
}
