package atlas.d2

import atlas.core.Replacement
import atlas.d2.internal.D2Writer
import atlas.test.ProjectLayout

internal fun d2Writer(
  layout: ProjectLayout,
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  classesRelativePath: String = "../classes.d2",
) = D2Writer(
  typedModules = layout.modules,
  links = layout.links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  pathToClassesFile = classesRelativePath,
)
