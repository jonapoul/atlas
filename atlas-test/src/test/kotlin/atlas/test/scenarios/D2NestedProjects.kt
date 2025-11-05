package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.KOTLIN_VERSION
import atlas.test.kotlinJvmBuildScript

internal object D2NestedProjects : D2Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "path:to:my:project" to kotlinJvmBuildScript,
  )
}
