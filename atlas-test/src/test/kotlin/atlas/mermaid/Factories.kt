package atlas.mermaid

import atlas.core.Replacement
import atlas.core.internal.ProjectLink
import atlas.core.internal.TypedProject
import atlas.mermaid.internal.MermaidWriter

internal fun mermaidWriter(
  typedProjects: Set<TypedProject> = emptySet(),
  links: Set<ProjectLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupProjects: Boolean = false,
  config: MermaidConfig = MermaidConfig(),
) = MermaidWriter(
  typedProjects = typedProjects,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupProjects = groupProjects,
  config = config,
)
