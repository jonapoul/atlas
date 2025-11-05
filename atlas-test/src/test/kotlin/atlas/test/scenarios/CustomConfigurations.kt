package atlas.test.scenarios

import atlas.test.Scenario
import atlas.test.kotlinJvmBuildScript

internal object CustomConfigurations : Scenario by OneKotlinJvmProject {
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
