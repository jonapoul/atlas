package atlas.test.scenarios

import atlas.test.AGP_VERSION
import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.androidBuildScript
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectsNoMatchingType : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        hasPluginId(name = "Won't match", color = "#123456", pluginId = "com.something.whatever")
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "a" to javaBuildScript,
    "b" to kotlinJvmBuildScript,
    "c" to androidBuildScript,
  )
}
