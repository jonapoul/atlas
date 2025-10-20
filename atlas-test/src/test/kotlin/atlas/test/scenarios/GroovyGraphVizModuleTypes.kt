package atlas.test.scenarios

import atlas.test.Scenario

internal object GroovyGraphVizModuleTypes : Scenario by GroovyBasic {
  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.atlas.graphviz'
    }

    atlas {
      moduleTypes {
        kotlinJvm {
          color = "mediumorchid"
          hasPluginId = "org.jetbrains.kotlin.jvm"
        }

        other {
          color = "gainsboro"
          pathMatches = ".*?"
        }
      }
    }
  """.trimIndent()
}
