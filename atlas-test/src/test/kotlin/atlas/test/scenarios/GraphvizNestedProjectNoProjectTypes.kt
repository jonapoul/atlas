package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario

internal object GraphvizNestedProjectNoProjectTypes : Scenario by GraphvizNestedProject {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }
  """.trimIndent()
}
