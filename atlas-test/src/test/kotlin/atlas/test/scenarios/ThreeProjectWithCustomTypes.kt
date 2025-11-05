package atlas.test.scenarios

import atlas.test.AGP_VERSION
import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.androidBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectWithCustomTypes : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      kotlin("android") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        create("Data") {
          color = "#ABC123"
          pathContains = "data"
        }
        create("Domain") {
          color = "#123ABC"
          pathMatches = ".*-domain"
        }
        create("Android") {
          color = "#A1B2C3"
          hasPluginId = "com.android.base"
        }
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "test-data" to kotlinJvmBuildScript,
    "test-domain" to kotlinJvmBuildScript,
    "test-ui" to androidBuildScript,
  )
}
