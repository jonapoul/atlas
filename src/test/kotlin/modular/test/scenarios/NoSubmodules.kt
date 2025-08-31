package modular.test.scenarios

import modular.test.Scenario
import modular.test.settingsFileRepositories

object NoSubmodules : Scenario {
  override val settingsFile = settingsFileRepositories

  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      moduleTypes {
        androidApp()
        kotlinMultiplatform()
        androidLibrary()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = emptyMap<String, String>()
}
