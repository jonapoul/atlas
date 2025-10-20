package atlas.mermaid

import atlas.core.Replacement
import atlas.core.internal.ModuleLink
import atlas.core.internal.TypedModule
import atlas.mermaid.internal.MermaidWriter

internal fun mermaidWriter(
  typedModules: Set<TypedModule> = emptySet(),
  links: Set<ModuleLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  config: MermaidConfig = MermaidConfig(),
) = MermaidWriter(
  typedModules = typedModules,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  config = config,
)
