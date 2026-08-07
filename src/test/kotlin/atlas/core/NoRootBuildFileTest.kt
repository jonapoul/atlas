package atlas.core

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.childExists
import atlas.test.contentContains
import atlas.test.noTasksFailed
import atlas.test.resolve
import atlas.test.scenarios.NoRootBuildFile
import blueprint.test.runTask
import kotlin.test.Test

/**
 * The root project is the one every subproject resolves the collated files and the shared legends
 * from, so it has to be wired whether or not it has a build file of its own. When it wasn't, those
 * artifacts resolved to nothing and the build died with `Collection is empty` while storing the
 * configuration cache - see https://github.com/jonapoul/atlas/issues/429.
 */
internal class NoRootBuildFileTest : ScenarioTest() {
  @Test
  fun `Collate links when the root has no build file`() =
    runScenario(NoRootBuildFile) {
      // when
      val result = runTask("writeProjectTree").build()

      // then
      assertThat(result).noTasksFailed()
      assertThat(resolve("a/build/atlas/project-tree.json")).contentContains(""""toPath":":b"""")
    }

  @Test
  fun `Generate every framework's chart when the root has no build file`() =
    runScenario(NoRootBuildFile) {
      // when
      val result =
        runTask("writeD2Chart", "writeGraphvizChart", "writeMermaidChart", "writeD2Classes").build()

      // then
      assertThat(result).noTasksFailed()
      assertThat(rootDir)
        .childExists("a/atlas/d2/chart.d2")
        .childExists("a/atlas/graphviz/chart.dot")
        .childExists("a/atlas/mermaid/chart.mmd")
        .childExists("atlas/d2/classes.d2")
    }
}
