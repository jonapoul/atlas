package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.KOTLIN_VERSION

internal object D2AllProjectTypes : D2Scenario by D2Basic {
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
        kotlinJvm()
        java()
        other()
      }
    }
  """.trimIndent()
}
