package atlas.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import atlas.core.internal.ProjectLink
import atlas.core.internal.readProjectLinks
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.isEqualToSet
import atlas.test.runTask
import atlas.test.scenarios.CustomConfigurationExcluded
import atlas.test.scenarios.CustomConfigurations
import atlas.test.scenarios.DiamondGraph
import atlas.test.scenarios.OneKotlinJvmProject
import atlas.test.scenarios.ThreeProjectsWithBuiltInTypes
import atlas.test.scenarios.TriangleGraph
import atlas.test.taskWasSuccessful
import kotlin.test.Test

internal class WriteProjectLinksTest : ScenarioTest() {
  @Test
  fun `Empty file for single project with no dependencies`() = runScenario(OneKotlinJvmProject) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-jvm:writeProjectLinks")

    // and the links file is empty
    assertThat(projectLinks(project = "test-jvm")).isEmpty()
  }

  @Test
  fun `Empty files for three projects with no dependencies`() = runScenario(ThreeProjectsWithBuiltInTypes) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result)
      .taskWasSuccessful(":test-data:writeProjectLinks")
      .taskWasSuccessful(":test-domain:writeProjectLinks")
      .taskWasSuccessful(":test-ui:writeProjectLinks")

    // and the links file is empty
    assertThat(projectLinks(project = "test-data")).isEmpty()
    assertThat(projectLinks(project = "test-domain")).isEmpty()
    assertThat(projectLinks(project = "test-ui")).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result)
      .taskWasSuccessful(":top:writeProjectLinks")
      .taskWasSuccessful(":mid-a:writeProjectLinks")
      .taskWasSuccessful(":mid-b:writeProjectLinks")
      .taskWasSuccessful(":bottom:writeProjectLinks")

    // and the links file is empty
    assertThat(projectLinks(project = "top")).isEqualToSet(
      ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
      ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
    )
    assertThat(projectLinks(project = "mid-a")).isEqualToSet(
      ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
    )
    assertThat(projectLinks(project = "mid-b")).isEqualToSet(
      ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
    )
    assertThat(projectLinks(project = "bottom")).isEqualTo(emptySet())
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected as expected
    assertThat(projectLinks(project = "a")).isEqualToSet(
      ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
      ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
    )
    assertThat(projectLinks(project = "b1")).isEqualToSet(
      ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
      ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
    )
    assertThat(projectLinks(project = "b2")).isEqualToSet(
      ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
      ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
    )
    assertThat(projectLinks(project = "c1")).isEmpty()
    assertThat(projectLinks(project = "c2")).isEmpty()
    assertThat(projectLinks(project = "c3")).isEmpty()
  }

  @Test
  fun `Custom configuration is picked up if we dont exclude it`() = runScenario(CustomConfigurations) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links
    assertThat(projectLinks(project = "a")).isEqualToSet(
      ProjectLink(fromPath = ":a", toPath = ":b", configuration = "abc", type = null),
      ProjectLink(fromPath = ":a", toPath = ":b", configuration = "xyz", type = null),
    )
    assertThat(projectLinks(project = "b")).isEmpty()
  }

  @Test
  fun `Custom configuration is excluded`() = runScenario(CustomConfigurationExcluded) {
    // when
    val result = runTask("writeProjectLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links, but the xyz config was excluded
    assertThat(projectLinks(project = "a")).isEqualToSet(
      ProjectLink(fromPath = ":a", toPath = ":b", configuration = "abc", type = null),
    )
    assertThat(projectLinks(project = "b")).isEmpty()
  }

  private fun projectLinks(project: String): Set<ProjectLink> = projectRoot
    .resolve("$project/build/atlas/project-links.json")
    .let(::readProjectLinks)
}
