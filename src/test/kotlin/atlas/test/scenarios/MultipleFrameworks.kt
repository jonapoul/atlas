package atlas.test.scenarios

import atlas.core.Framework
import atlas.test.Scenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

/** Every framework switched on at once, all generating from the same project and link types. */
internal object MultipleFrameworks : Scenario {
  override val frameworks = Framework.entries.toSet()

  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
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
