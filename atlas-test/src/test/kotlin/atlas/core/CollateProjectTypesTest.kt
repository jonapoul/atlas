package atlas.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import atlas.core.internal.TypedProject
import atlas.core.internal.readProjectTypes
import atlas.test.ScenarioTest
import atlas.test.isEqualToSet
import atlas.test.runTask
import atlas.test.scenarios.NoSubprojects
import atlas.test.scenarios.PathMatches
import atlas.test.scenarios.ThreeProjectWithCustomTypes
import atlas.test.scenarios.ThreeProjectsWithBuiltInTypes
import atlas.test.taskWasSuccessful
import java.io.File
import kotlin.test.Test

internal class CollateProjectTypesTest : ScenarioTest() {
  @Test
  fun `Collate three custom types`() = runScenario(ThreeProjectWithCustomTypes) {
    // when
    val result = runTask("collateProjectTypes").build()

    // then three dependent tasks were run
    assertThat(result).taskWasSuccessful(":test-data:writeProjectType")
    assertThat(result).taskWasSuccessful(":test-domain:writeProjectType")
    assertThat(result).taskWasSuccessful(":test-ui:writeProjectType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateProjectTypes")

    // and the types were aggregated in the root project's build dir
    assertThat(projectTypes).isEqualToSet(
      TypedProject(projectPath = ":test-data", type = ProjectType(name = "Data", color = "#ABC123")),
      TypedProject(projectPath = ":test-domain", type = ProjectType(name = "Domain", color = "#123ABC")),
      TypedProject(projectPath = ":test-ui", type = ProjectType(name = "Android", color = "#A1B2C3")),
    )
  }

  @Test
  fun `Collate three built in types`() = runScenario(ThreeProjectsWithBuiltInTypes) {
    // when
    val result = runTask("collateProjectTypes").build()

    // then three dependent tasks were run
    assertThat(result)
      .taskWasSuccessful(":test-data:writeProjectType")
      .taskWasSuccessful(":test-domain:writeProjectType")
      .taskWasSuccessful(":test-ui:writeProjectType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateProjectTypes")

    // and the types were aggregated in the root project's build dir
    assertThat(projectTypes).isEqualToSet(
      TypedProject(projectPath = ":test-data", type = ProjectType(name = "Java", color = "orange")),
      TypedProject(projectPath = ":test-domain", type = ProjectType(name = "Kotlin JVM", color = "mediumorchid")),
      TypedProject(projectPath = ":test-ui", type = ProjectType(name = "Android Library", color = "lightgreen")),
    )
  }

  @Test
  fun `Collate with no subprojects`() = runScenario(NoSubprojects) {
    // when
    val result = runTask("collateProjectTypes").build()

    // then no write tasks were run
    val taskPaths = result.tasks.map { it.path }
    assertThat(taskPaths).isEqualTo(listOf(":collateProjectTypes"))

    // and no types were collated, but the task still passed
    assertThat(result).taskWasSuccessful(":collateProjectTypes")
    assertThat(projectTypes).isEmpty()
  }

  @Test
  fun `Match with regex options`() = runScenario(PathMatches) {
    // when
    runTask("collateProjectTypes").build()

    // then
    assertThat(projectTypes).isEqualToSet(
      TypedProject(projectPath = ":Test-X", type = ProjectType(name = "B", color = "limegreen")),
      TypedProject(projectPath = ":a1-B2-C3", type = ProjectType(name = "D", color = "gainsboro")),
      TypedProject(projectPath = ":abc123", type = ProjectType(name = "A", color = "orange")),
      TypedProject(projectPath = ":foo-bar", type = ProjectType(name = "E", color = "mediumorchid")),
      TypedProject(projectPath = ":hello", type = ProjectType(name = "C", color = "mediumslateblue")),
    )
  }

  private val File.projectTypes get() = resolve("build/atlas/project-types.json").let(::readProjectTypes)
}
