package atlas.test.scenarios

import atlas.test.Scenario

internal object OverrideModuleLinksFile : Scenario by DiamondGraph {
  override val rootBuildFile = """
      import atlas.core.tasks.CollateModuleLinks

      ${DiamondGraph.rootBuildFile}

      tasks.withType(CollateModuleLinks::class.java).configureEach {
        outputFile = file("custom-module-links-file.txt")
      }
  """.trimIndent()
}
