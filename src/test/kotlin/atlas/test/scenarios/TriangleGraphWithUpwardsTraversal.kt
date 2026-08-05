package atlas.test.scenarios

import atlas.test.Scenario

internal object TriangleGraphWithUpwardsTraversal : Scenario by TriangleGraph {
  override val atlasConfig =
    """
    alsoTraverseUpwards = true

    projectTypes {
      kotlinJvm()
    }
    """
      .trimIndent()
}
