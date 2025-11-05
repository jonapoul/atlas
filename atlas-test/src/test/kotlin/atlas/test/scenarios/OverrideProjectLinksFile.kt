package atlas.test.scenarios

import atlas.test.Scenario

internal object OverrideProjectLinksFile : Scenario by DiamondGraph {
  override val rootBuildFile = """
      import atlas.core.tasks.CollateProjectLinks

      ${DiamondGraph.rootBuildFile}

      tasks.withType(CollateProjectLinks::class.java).configureEach {
        outputFile = file("custom-project-links-file.txt")
      }
  """.trimIndent()
}
