package atlas.graphviz

import atlas.core.Replacement
import atlas.core.internal.ProjectLink
import atlas.core.internal.TypedProject
import atlas.graphviz.internal.DotWriter

internal fun dotWriter(
  typedProjects: Set<TypedProject> = emptySet(),
  links: Set<ProjectLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupProjects: Boolean = false,
  config: DotConfig = DotConfig(),
) = DotWriter(
  typedProjects = typedProjects,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupProjects = groupProjects,
  config = config,
)
