/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import modular.core.internal.ModuleLink
import modular.core.internal.TypedModule

internal interface ProjectLayout {
  val modules: Set<TypedModule>
  val links: Set<ModuleLink>
}

internal object OneLevelOfSubmodules : ProjectLayout {
  override val modules = setOf(
    typedModule(path = ":app"),
    typedModule(path = ":data:a"),
    typedModule(path = ":data:b"),
    typedModule(path = ":domain:a"),
    typedModule(path = ":domain:b"),
    typedModule(path = ":ui:a"),
    typedModule(path = ":ui:b"),
    typedModule(path = ":ui:c"),
  )

  override val links = setOf(
    moduleLink(fromPath = ":app", toPath = ":ui:a"),
    moduleLink(fromPath = ":app", toPath = ":ui:b"),
    moduleLink(fromPath = ":app", toPath = ":ui:c"),
    moduleLink(fromPath = ":domain:a", toPath = ":data:a"),
    moduleLink(fromPath = ":domain:b", toPath = ":data:a"),
    moduleLink(fromPath = ":domain:b", toPath = ":data:b"),
    moduleLink(fromPath = ":ui:a", toPath = ":domain:a"),
    moduleLink(fromPath = ":ui:b", toPath = ":domain:b"),
    moduleLink(fromPath = ":ui:c", toPath = ":domain:a"),
    moduleLink(fromPath = ":ui:c", toPath = ":domain:b"),
  )
}

internal object TwoLevelsOfSubmodules : ProjectLayout {
  override val modules = setOf(
    typedModule(path = ":app"),
    typedModule(path = ":data:a"),
    typedModule(path = ":data:b"),
    typedModule(path = ":data:sub:sub1"),
    typedModule(path = ":data:sub:sub2"),
    typedModule(path = ":domain:a"),
    typedModule(path = ":domain:b"),
    typedModule(path = ":ui:a"),
    typedModule(path = ":ui:b"),
    typedModule(path = ":ui:c"),
  )

  override val links = setOf(
    moduleLink(fromPath = ":app", toPath = ":ui:a"),
    moduleLink(fromPath = ":app", toPath = ":ui:b"),
    moduleLink(fromPath = ":app", toPath = ":ui:c"),
    moduleLink(fromPath = ":domain:a", toPath = ":data:a"),
    moduleLink(fromPath = ":domain:a", toPath = ":data:sub:sub1"),
    moduleLink(fromPath = ":domain:a", toPath = ":data:sub:sub2"),
    moduleLink(fromPath = ":domain:b", toPath = ":data:a"),
    moduleLink(fromPath = ":domain:b", toPath = ":data:b"),
    moduleLink(fromPath = ":ui:a", toPath = ":domain:a"),
    moduleLink(fromPath = ":ui:b", toPath = ":domain:b"),
    moduleLink(fromPath = ":ui:c", toPath = ":domain:a"),
    moduleLink(fromPath = ":ui:c", toPath = ":domain:b"),
  )
}

internal object ModuleWithNoLinks : ProjectLayout {
  override val modules = setOf(
    typedModule(path = ":app"),
  )

  override val links = emptySet<ModuleLink>()
}

internal object Abc : ProjectLayout {
  override val modules = setOf(
    typedModule(path = ":a"),
    typedModule(path = ":b"),
    typedModule(path = ":c"),
  )

  override val links = setOf(
    moduleLink(fromPath = ":a", toPath = ":b"),
    moduleLink(fromPath = ":a", toPath = ":c"),
  )
}
