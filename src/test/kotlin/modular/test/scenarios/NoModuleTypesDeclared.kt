package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario
import modular.test.settingsFileRepositories

object NoModuleTypesDeclared : Scenario {
  override val settingsFile = """
    $settingsFileRepositories
    include(":test-jvm")
  """.trimIndent()

  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-jvm" to BASIC_JVM_BUILD_SCRIPT,
  )
}
