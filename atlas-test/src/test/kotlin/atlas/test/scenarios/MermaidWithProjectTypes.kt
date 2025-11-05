package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario

internal object MermaidWithProjectTypes : Scenario by MermaidBasic {
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
}
