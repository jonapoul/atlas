package atlas.test.scenarios

import atlas.test.Scenario

internal object MermaidWithGroupsNotNested : Scenario by MermaidBasic {
  override val atlasConfig = "groupProjects = true"
}
