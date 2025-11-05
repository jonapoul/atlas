package atlas.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import atlas.core.internal.TypedProject
import atlas.core.internal.readProjectType
import atlas.test.ScenarioTest
import atlas.test.androidHomeOrSkip
import atlas.test.buildRunner
import atlas.test.runTask
import atlas.test.scenarios.ProjectTypesDeclaredButNoneMatch
import atlas.test.scenarios.NoProjectTypesDeclared
import atlas.test.scenarios.OneKotlinJvmProject
import atlas.test.scenarios.ThreeProjectsNoMatchingType
import atlas.test.scenarios.ThreeProjectsOnlyMatchingOther
import atlas.test.scenarios.ThreeProjectWithCustomTypes
import atlas.test.taskHadResult
import atlas.test.taskWasSuccessful
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import java.io.File
import kotlin.test.Test

internal class WriteProjectTypeTest : ScenarioTest() {
  @Test
  fun `No project types declared`() = runScenario(NoProjectTypesDeclared) {
    // when
    val result = runTask("writeProjectType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeProjectType", SUCCESS)
    assertThat(projectType("test-jvm")).isEqualTo(TypedProject(":test-jvm", type = null))
  }

  @Test
  fun `Project types declared but none match`() = runScenario(ProjectTypesDeclaredButNoneMatch) {
    // when
    val result = runTask("writeProjectType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeProjectType", SUCCESS)
    assertThat(projectType("test-jvm")).isEqualTo(TypedProject(":test-jvm", type = null))
  }

  @Test
  fun `Write file if built-in type matches`() = runScenario(OneKotlinJvmProject) {
    // when
    val result = runTask("writeProjectType").build()

    // then
    assertThat(result).taskWasSuccessful(":test-jvm:writeProjectType")
    assertThat(projectType("test-jvm"))
      .isEqualTo(TypedProject(":test-jvm", type = ProjectType("Kotlin JVM", color = "mediumorchid")))

    // when running again, then it's cached
    val result2 = runTask("writeProjectType").build()
    assertThat(result2).taskHadResult(":test-jvm:writeProjectType", UP_TO_DATE)
  }

  @Test
  fun `Write files if custom types match`() = runScenario(ThreeProjectWithCustomTypes) {
    // when
    val result = buildRunner(androidHomeOrSkip())
      .runTask("writeProjectType")
      .build()

    // then
    assertThat(result)
      .taskWasSuccessful(":test-data:writeProjectType")
      .taskWasSuccessful(":test-domain:writeProjectType")
      .taskWasSuccessful(":test-ui:writeProjectType")

    assertThat(projectType("test-data"))
      .isEqualTo(TypedProject(":test-data", type = ProjectType("Data", color = "#ABC123")))
    assertThat(projectType("test-domain"))
      .isEqualTo(TypedProject(":test-domain", type = ProjectType("Domain", color = "#123ABC")))
    assertThat(projectType("test-ui"))
      .isEqualTo(TypedProject(":test-ui", type = ProjectType("Android", color = "#A1B2C3")))
  }

  @Test
  fun `Fall back to other if no types match`() = runScenario(ThreeProjectsOnlyMatchingOther) {
    // when
    runTask("writeProjectType", androidHomeOrSkip()).build()

    // then
    assertThat(projectType("a")).isEqualTo(TypedProject(":a", type = ProjectType("Other", color = "gainsboro")))
    assertThat(projectType("b")).isEqualTo(TypedProject(":b", type = ProjectType("Other", color = "gainsboro")))
    assertThat(projectType("c")).isEqualTo(TypedProject(":c", type = ProjectType("Other", color = "gainsboro")))
  }

  @Test
  fun `No types match`() = runScenario(ThreeProjectsNoMatchingType) {
    // when
    val result = runTask("a:writeProjectType", androidHomeOrSkip()).build()

    // then
    assertThat(result).taskHadResult(":a:writeProjectType", SUCCESS)
    assertThat(projectType("a")).isEqualTo(TypedProject(":a", type = null))
  }

  private fun File.projectType(path: String) =
    resolve("$path/build/atlas/project-type.json")
      .let(::readProjectType)
}
