package atlas.test.scenarios

import atlas.test.MermaidScenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object MermaidBasic : MermaidScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to
        """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
      }
    """
          .trimIndent(),
      "b" to javaBuildScript,
      "c" to javaBuildScript,
    )
}
