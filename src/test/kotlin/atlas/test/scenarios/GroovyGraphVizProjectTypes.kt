package atlas.test.scenarios

import atlas.test.Scenario

internal object GroovyGraphVizProjectTypes : Scenario by GroovyBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm {
        color = "mediumorchid"
        hasPluginId = "org.jetbrains.kotlin.jvm"
      }

      other {
        color = "gainsboro"
        pathMatches = ".*?"
      }
    }
    """
      .trimIndent()
}
