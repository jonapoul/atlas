package atlas

import assertk.assertThat
import assertk.assertions.contains
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
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
    assertThat(check).allTasksSuccessful()
  }

  private fun File.subprojectFailsTestCase() {
    val result = runTask(":a:atlasGenerate").buildAndFail()

    assertThat(result.output).contains("atlasGenerate is disabled because org.gradle.configureondemand is enabled.")
    assertThat(result.output).contains("you can only run atlasGenerate on the root project, not on :a")
  }
}
