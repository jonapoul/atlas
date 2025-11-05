package atlas.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import atlas.core.internal.ProjectLink
import atlas.core.internal.readProjectLinks
import atlas.test.ScenarioTest
import atlas.test.allSuccessful
import atlas.test.runTask
import atlas.test.scenarios.DiamondGraph
import atlas.test.scenarios.DiamondGraphWithUpwardsTraversal
import atlas.test.scenarios.OneKotlinJvmProject
import atlas.test.scenarios.ThreeProjectsWithBuiltInTypes
import atlas.test.scenarios.TriangleGraph
import atlas.test.scenarios.TriangleGraphWithUpwardsTraversal
import kotlin.test.Test

internal class WriteProjectTreeTest : ScenarioTest() {
  @Test
  fun `Empty files for single project with no dependencies`() = runScenario(OneKotlinJvmProject) {
    // when
    val result = runTask("writeProjectTree")
      .withPluginClasspath()
      .build()

    // then
    assertThat(result.tasks).allSuccessful()

    // and the tree file is empty
    assertThat(projectTree("test-jvm")).isEmpty()
  }

  @Test
  fun `Empty files for three projects with no dependencies`() = runScenario(ThreeProjectsWithBuiltInTypes) {
    // when
    val result = runTask("writeProjectTree").build()

    // then the tasks were run
    assertThat(result.tasks).allSuccessful()

    // and the tree files are empty
    assertThat(projectTree("test-data")).isEmpty()
    assertThat(projectTree("test-domain")).isEmpty()
    assertThat(projectTree("test-ui")).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("writeProjectTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top project sees everything below it
    assertThat(projectTree("top")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the mid only sees itself and bottom
    assertThat(projectTree("mid-a")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
      ),
    )

    assertThat(projectTree("mid-b")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
      ),
    )

    // and the bottom sees nothing
    assertThat(projectTree("bottom")).isEmpty()
  }

  @Test
  fun `Single links for diamond with upwards traversal`() = runScenario(DiamondGraphWithUpwardsTraversal) {
    // when
    val result = runTask("writeProjectTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top project sees everything below it
    assertThat(projectTree("top")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the mid sees itself, top and bottom
    assertThat(projectTree("mid-a")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
      ),
    )

    assertThat(projectTree("mid-b")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the bottom sees everything above it
    assertThat(projectTree("bottom")).isEqualTo(
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
    val result = runTask("writeProjectTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(projectTree("a")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("b1")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("b2")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("c1")).isEmpty()
    assertThat(projectTree("c2")).isEmpty()
    assertThat(projectTree("c3")).isEmpty()
  }

  @Test
  fun `Multiple links for triangle with upwards traversal`() = runScenario(TriangleGraphWithUpwardsTraversal) {
    // when
    val result = runTask("writeProjectTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(projectTree("a")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("b1")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("b2")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("c1")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("c2")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(projectTree("c3")).isEqualTo(
      setOf(
        ProjectLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ProjectLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
  }

  private fun projectTree(project: String): Set<ProjectLink> = projectRoot
    .resolve("$project/build/atlas/project-tree.json")
    .let(::readProjectLinks)
}
