package atlas.test.scenarios

import atlas.test.Scenario
import atlas.test.kotlinJvmBuildScript

internal object CustomConfigurationExcluded : Scenario by CustomConfigurations {
  override val rootBuildFile: String
    get() = """
      ${CustomConfigurations.rootBuildFile}

      atlas {
        ignoredConfigs.add("xyz")
      }
    """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "a" to """
      $kotlinJvmBuildScript

      val abc by configurations.creating
      val xyz by configurations.creating

      dependencies {
        abc(project(":b"))
        xyz(project(":b"))
      }
    """.trimIndent(),

    "b" to kotlinJvmBuildScript,
  )
}
