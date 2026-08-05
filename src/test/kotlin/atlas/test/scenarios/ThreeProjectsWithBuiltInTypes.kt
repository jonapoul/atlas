package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.androidBuildScript
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeProjectsWithBuiltInTypes : GraphvizScenario {
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
      androidLibrary()
      kotlinJvm()
      java()
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "test-data" to javaBuildScript,
      "test-domain" to kotlinJvmBuildScript,
      "test-ui" to androidBuildScript,
    )
}
