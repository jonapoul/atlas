package atlas.test

import atlas.core.ProjectType
import atlas.core.internal.ProjectLink
import atlas.core.internal.TypedProject

internal interface ProjectLayout {
  val projects: Set<TypedProject>
  val links: Set<ProjectLink>
}

internal object OneLevelOfSubprojects : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":app"),
    typedProject(path = ":data:a"),
    typedProject(path = ":data:b"),
    typedProject(path = ":domain:a"),
    typedProject(path = ":domain:b"),
    typedProject(path = ":ui:a"),
    typedProject(path = ":ui:b"),
    typedProject(path = ":ui:c"),
  )

  override val links = setOf(
    projectLink(fromPath = ":app", toPath = ":ui:a"),
    projectLink(fromPath = ":app", toPath = ":ui:b"),
    projectLink(fromPath = ":app", toPath = ":ui:c"),
    projectLink(fromPath = ":domain:a", toPath = ":data:a"),
    projectLink(fromPath = ":domain:b", toPath = ":data:a"),
    projectLink(fromPath = ":domain:b", toPath = ":data:b"),
    projectLink(fromPath = ":ui:a", toPath = ":domain:a"),
    projectLink(fromPath = ":ui:b", toPath = ":domain:b"),
    projectLink(fromPath = ":ui:c", toPath = ":domain:a"),
    projectLink(fromPath = ":ui:c", toPath = ":domain:b"),
  )
}

internal object LowestLevelOfSubprojects : ProjectLayout {
  override val projects = setOf(typedProject(path = ":ui:c"))
  override val links = emptySet<ProjectLink>()
}

internal object OneLevelOfSubprojectsWithReplacements : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":app"),
    typedProject(path = ":projects:data:a"),
    typedProject(path = ":projects:data:b"),
    typedProject(path = ":projects:domain:a"),
    typedProject(path = ":projects:domain:b"),
    typedProject(path = ":projects:ui:a"),
    typedProject(path = ":projects:ui:b"),
    typedProject(path = ":projects:ui:c"),
  )

  override val links = setOf(
    projectLink(fromPath = ":app", toPath = ":projects:ui:a"),
    projectLink(fromPath = ":app", toPath = ":projects:ui:b"),
    projectLink(fromPath = ":app", toPath = ":projects:ui:c"),
    projectLink(fromPath = ":projects:domain:a", toPath = ":projects:data:a"),
    projectLink(fromPath = ":projects:domain:b", toPath = ":projects:data:a"),
    projectLink(fromPath = ":projects:domain:b", toPath = ":projects:data:b"),
    projectLink(fromPath = ":projects:ui:a", toPath = ":projects:domain:a"),
    projectLink(fromPath = ":projects:ui:b", toPath = ":projects:domain:b"),
    projectLink(fromPath = ":projects:ui:c", toPath = ":projects:domain:a"),
    projectLink(fromPath = ":projects:ui:c", toPath = ":projects:domain:b"),
  )
}

internal object TwoLevelsOfSubprojects : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":app"),
    typedProject(path = ":data:a"),
    typedProject(path = ":data:b"),
    typedProject(path = ":data:sub:sub1"),
    typedProject(path = ":data:sub:sub2"),
    typedProject(path = ":domain:a"),
    typedProject(path = ":domain:b"),
    typedProject(path = ":ui:a"),
    typedProject(path = ":ui:b"),
    typedProject(path = ":ui:c"),
  )

  override val links = setOf(
    projectLink(fromPath = ":app", toPath = ":ui:a"),
    projectLink(fromPath = ":app", toPath = ":ui:b"),
    projectLink(fromPath = ":app", toPath = ":ui:c"),
    projectLink(fromPath = ":domain:a", toPath = ":data:a"),
    projectLink(fromPath = ":domain:a", toPath = ":data:sub:sub1"),
    projectLink(fromPath = ":domain:a", toPath = ":data:sub:sub2"),
    projectLink(fromPath = ":domain:b", toPath = ":data:a"),
    projectLink(fromPath = ":domain:b", toPath = ":data:b"),
    projectLink(fromPath = ":ui:a", toPath = ":domain:a"),
    projectLink(fromPath = ":ui:b", toPath = ":domain:b"),
    projectLink(fromPath = ":ui:c", toPath = ":domain:a"),
    projectLink(fromPath = ":ui:c", toPath = ":domain:b"),
  )
}

internal object ProjectWithNoLinks : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":app", type = ProjectType(name = "red", color = "red")),
  )

  override val links = emptySet<ProjectLink>()
}

internal object SingleNestedProjectWithNoLinks : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":a:b", type = ProjectType(name = "red", color = "red")),
  )

  override val links = emptySet<ProjectLink>()
}

internal object Abc : ProjectLayout {
  override val projects = setOf(
    typedProject(path = ":a"),
    typedProject(path = ":b"),
    typedProject(path = ":c"),
  )

  override val links = setOf(
    projectLink(fromPath = ":a", toPath = ":b"),
    projectLink(fromPath = ":a", toPath = ":c"),
  )
}
