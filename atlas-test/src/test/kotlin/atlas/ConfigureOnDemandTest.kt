package atlas

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.contains
import atlas.test.noTasksFailed
import atlas.test.runTask
import atlas.test.scenarios.D2ConfigureOnDemand
import atlas.test.scenarios.GraphvizConfigureOnDemand
import atlas.test.scenarios.MermaidConfigureOnDemand
import java.io.File
import kotlin.test.Test

internal class ConfigureOnDemandTest : ScenarioTest() {
  @Test fun `D2 configureOnDemand on root project`() =
    runScenario(D2ConfigureOnDemand) { rootProjectTestCase() }

  @Test fun `Graphviz configureOnDemand on root project`() =
    runScenario(GraphvizConfigureOnDemand) { rootProjectTestCase() }

  @Test fun `Mermaid configureOnDemand on root project`() =
    runScenario(MermaidConfigureOnDemand) { rootProjectTestCase() }

  @Test fun `D2 configureOnDemand on subproject fails`() =
    runScenario(D2ConfigureOnDemand) { subprojectFailsTestCase() }

  @Test fun `Graphviz configureOnDemand on subproject fails`() =
    runScenario(GraphvizConfigureOnDemand) { subprojectFailsTestCase() }

  @Test fun `Mermaid configureOnDemand on subproject fails`() =
    runScenario(MermaidConfigureOnDemand) { subprojectFailsTestCase() }

  private fun File.rootProjectTestCase() {
    // When configure-on-demand is enabled, running from root should still work normally
    val generate = runTask("atlasGenerate").build()
    assertThat(generate).allTasksSuccessful()

    val check = runTask("atlasCheck").build()
    assertThat(check).noTasksFailed() // some cached from the generation step before
  }

  private fun File.subprojectFailsTestCase() {
    val generate = runTask(":a:atlasGenerate").buildAndFail()
    assertThat(generate.output)
      .contains("atlasGenerate is disabled when run on a subproject because org.gradle.configureondemand is enabled.")
      .contains("you can only run atlasGenerate on the root project, not on :a")

    val check = runTask(":a:atlasCheck").buildAndFail()
    assertThat(check.output)
      .contains("atlasCheck is disabled when run on a subproject because org.gradle.configureondemand is enabled.")
      .contains("you can only run atlasCheck on the root project, not on :a")
  }
}
