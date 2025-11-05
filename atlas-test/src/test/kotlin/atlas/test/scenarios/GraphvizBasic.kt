package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object GraphvizBasic : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
        java()
        hasPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
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
