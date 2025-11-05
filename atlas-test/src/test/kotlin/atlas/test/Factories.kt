package atlas.test

import atlas.core.LinkType
import atlas.core.ProjectType
import atlas.core.StringEnum
import atlas.core.internal.Node
import atlas.core.internal.ProjectLink
import atlas.core.internal.TypedProject

internal fun node(
  path: String,
  type: ProjectType? = null,
) = Node(typedProject(path, type))

internal fun typedProject(
  path: String,
  type: ProjectType? = null,
) = TypedProject(
  projectPath = path,
  type = type,
)

internal fun projectLink(
  fromPath: String,
  toPath: String,
  configuration: String = "implementation",
  style: StringEnum? = null,
  color: String? = null,
) = ProjectLink(
  fromPath,
  toPath,
  configuration,
  type = LinkType(configuration, style?.string, color),
)
