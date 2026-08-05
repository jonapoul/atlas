package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.androidBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectWithCustomTypes : GraphvizScenario {
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
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "test-data" to kotlinJvmBuildScript,
      "test-domain" to kotlinJvmBuildScript,
      "test-ui" to androidBuildScript,
    )
}
