package atlas.graphviz

import atlas.core.Replacement
import atlas.core.internal.ModuleLink
import atlas.core.internal.TypedModule
import atlas.graphviz.internal.DotWriter

internal fun dotWriter(
  typedModules: Set<TypedModule> = emptySet(),
  links: Set<ModuleLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  config: DotConfig = DotConfig(),
) = DotWriter(
  typedModules = typedModules,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  config = config,
)
