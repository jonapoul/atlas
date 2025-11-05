package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION

internal object ProjectTypeCreated : GraphvizScenario by GraphvizBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        create("custom") {
          hasPluginId = "a.b.c"
        }
      }
    }
  """.trimIndent()
}
