package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.KOTLIN_VERSION
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object D2Basic : D2Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
        java()
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "a" to """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
      }
    """.trimIndent(),

    "b" to javaBuildScript,

    "c" to javaBuildScript,
  )
}
