package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.androidBuildScript
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectsOnlyMatchingOther : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
      id("com.android.library") apply false
    }
    """
      .trimIndent()

  override val atlasConfig =
    """
    projectTypes {
      hasPluginId(name = "Won't match", color = "#123456", pluginId = "com.something.whatever")
      other()
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to javaBuildScript,
      "b" to kotlinJvmBuildScript,
      "c" to androidBuildScript,
    )
}
