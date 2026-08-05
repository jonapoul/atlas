package atlas.test.scenarios

import atlas.test.Scenario

internal object MermaidWithoutGroups : Scenario by MermaidBasic {
  override val atlasConfig = "groupProjects = false"
}
