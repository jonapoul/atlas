package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object GraphvizBasic : GraphvizScenario {
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
      hasPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
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
