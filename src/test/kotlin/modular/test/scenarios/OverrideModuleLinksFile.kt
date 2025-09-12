package modular.test.scenarios

import modular.test.Scenario

object OverrideModuleLinksFile : Scenario by DiamondGraph {
  override val rootBuildFile = """
      import modular.tasks.CollateModuleLinksTask

      ${DiamondGraph.rootBuildFile}

      tasks.withType(CollateModuleLinksTask::class.java).configureEach {
        outputFile = file("custom-module-links-file.txt")
      }
  """.trimIndent()
}
