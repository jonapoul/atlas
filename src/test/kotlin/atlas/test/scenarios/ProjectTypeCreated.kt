package atlas.test.scenarios

import atlas.test.GraphvizScenario

internal object ProjectTypeCreated : GraphvizScenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      create("custom") {
        hasPluginId = "a.b.c"
      }
    }
    """
      .trimIndent()
}
