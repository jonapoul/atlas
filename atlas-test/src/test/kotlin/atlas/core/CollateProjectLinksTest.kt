package atlas.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import atlas.core.internal.ProjectLink
import atlas.core.internal.readProjectLinks
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.doesNotExist
import atlas.test.runTask
import atlas.test.scenarios.DiamondGraph
import atlas.test.scenarios.OneKotlinJvmProject
import atlas.test.scenarios.OverrideProjectLinksFile
import atlas.test.scenarios.ThreeProjectsWithBuiltInTypes
import atlas.test.scenarios.TriangleGraph
import atlas.test.taskWasSuccessful
import java.io.File
import kotlin.test.Test

internal class CollateProjectLinksTest : ScenarioTest() {
  @Test
  fun `Empty file for single project with no dependencies`() = runScenario(OneKotlinJvmProject) {
    // when
    val result = runTask("collateProjectLinks").build()

    // then the task was run
    assertThat(result)
      .taskWasSuccessful(":test-jvm:writeProjectLinks")
      .taskWasSuccessful(":collateProjectLinks")

    // and the links file is empty
    assertThat(projectLinks).isEmpty()
  }

  @Test
  fun `Empty file for three projects with no dependencies`() = runScenario(ThreeProjectsWithBuiltInTypes) {
    // when
    val result = runTask("collateProjectLinks").build()

    // then the tasks were run
    assertThat(result.tasks).allSuccessful()

    // and the links file is empty
    assertThat(projectLinks).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("collateProjectLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the links file contains the expected, in a-z order
    assertThat(projectLinks).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("collateProjectLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(projectLinks).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
  }

  @Test
  fun `Can override task conventions from build script`() = runScenario(OverrideProjectLinksFile) {
    // when
    runTask("collateProjectLinks").build()

    // then the default config file wasn't created
    assertThat(projectLinksFile).doesNotExist()

    // but the custom one does
    val customProjectLinksFileContents = readProjectLinks(resolve("custom-project-links-file.txt"))

    // and it contains the same from the diamond test a bit up from here
    assertThat(customProjectLinksFileContents).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )
  }

  private val projectLinksFile: File
    get() = projectRoot.resolve("build/atlas/project-links.json")

  private val projectLinks: Set<ProjectLink>
    get() = readProjectLinks(projectLinksFile)
}
