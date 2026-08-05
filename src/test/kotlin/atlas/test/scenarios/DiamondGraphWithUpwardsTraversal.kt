package atlas.test.scenarios

import atlas.test.Scenario

internal object DiamondGraphWithUpwardsTraversal : Scenario by DiamondGraph {
  override val atlasConfig =
    """
    alsoTraverseUpwards = true
    projectTypes {
      kotlinJvm()
    }
    """
      .trimIndent()
}
