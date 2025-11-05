package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.kotlinJvmBuildScript

internal object ProjectTypesDeclaredButNoneMatch : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        androidApp()
        kotlinMultiplatform()
        androidLibrary()
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "test-jvm" to kotlinJvmBuildScript,
  )
}
