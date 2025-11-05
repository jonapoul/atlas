package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.MermaidScenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object MermaidBasic : MermaidScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
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
