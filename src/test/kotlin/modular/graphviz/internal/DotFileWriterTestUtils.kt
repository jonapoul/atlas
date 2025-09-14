/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("MatchingDeclarationName")

package modular.graphviz.internal

import modular.test.moduleLink
import modular.test.typedModule

internal object OneLevelOfSubmodules {
  val modules = setOf(
    typedModule(path = ":app"),
    typedModule(path = ":data:a"),
    typedModule(path = ":data:b"),
    typedModule(path = ":domain:a"),
    typedModule(path = ":domain:b"),
    typedModule(path = ":ui:a"),
    typedModule(path = ":ui:b"),
    typedModule(path = ":ui:c"),
  )

  val links = setOf(
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

internal object TwoLevelsOfSubmodules {
  val modules = setOf(
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

  val links = setOf(
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
