package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION

internal object ModuleTypeCreated : GraphvizScenario by GraphvizBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        create("custom") {
          hasPluginId = "a.b.c"
        }
      }
    }
  """.trimIndent()
}
