package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object CustomConfigurationExcluded : Scenario by CustomConfigurations {
  override val rootBuildFile: String
    get() = """
      ${CustomConfigurations.rootBuildFile}

      modular {
        ignoredConfigs.add("xyz")
      }
    """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      $BASIC_JVM_BUILD_SCRIPT

      val abc by configurations.creating
      val xyz by configurations.creating

      dependencies {
        abc(project(":b"))
        xyz(project(":b"))
      }
    """.trimIndent(),

    "b" to BASIC_JVM_BUILD_SCRIPT,
  )
}
