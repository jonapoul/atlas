package atlas.test.scenarios

import atlas.test.AGP_VERSION
import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.androidBuildScript
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectsWithBuiltInTypes : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        androidLibrary()
        kotlinJvm()
        java()
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "test-data" to javaBuildScript,
    "test-domain" to kotlinJvmBuildScript,
    "test-ui" to androidBuildScript,
  )
}
