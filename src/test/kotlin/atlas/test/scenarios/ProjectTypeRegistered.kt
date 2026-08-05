package atlas.test.scenarios

import atlas.test.GraphvizScenario

internal object ProjectTypeRegistered : GraphvizScenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      register("custom") {
        hasPluginId = "a.b.c"
      }
    }
    """
      .trimIndent()
}
