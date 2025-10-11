/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import assertk.assertThat
import assertk.assertions.isEqualTo
import modular.core.ModuleType
import modular.core.internal.Subgraph
import modular.core.internal.buildGraphElements
import modular.test.OneLevelOfSubmodules
import modular.test.SingleNestedModuleWithNoLinks
import modular.test.TwoLevelsOfSubmodules
import modular.test.node
import kotlin.test.Test

internal class GraphHierarchyTest {
  @Test
  internal fun `Single-level groups`() {
    val dataSubGraph = Subgraph(
      path = listOf("data"),
      elements = mutableListOf(
        node(path = ":data:a"),
        node(path = ":data:b"),
      ),
    )
    val domainSubGraph = Subgraph(
      path = listOf("domain"),
      elements = mutableListOf(
        node(path = ":domain:a"),
        node(path = ":domain:b"),
      ),
    )
    val uiSubGraph = Subgraph(
      path = listOf("ui"),
      elements = mutableListOf(
        node(path = ":ui:a"),
        node(path = ":ui:b"),
        node(path = ":ui:c"),
      ),
    )

    val elements = buildGraphElements(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      thisPath = ":app",
    )
    assertThat(elements).isEqualTo(
      listOf(
        node(path = ":app"),
        dataSubGraph,
        domainSubGraph,
        uiSubGraph,
      ),
    )
  }

  @Test
  internal fun `Multi-level groups`() {
    val dataSubGraph = Subgraph(
      path = listOf("data"),
      elements = mutableListOf(
        node(path = ":data:a"),
        node(path = ":data:b"),
        Subgraph(
          path = listOf("data", "sub"),
          elements = listOf(
            node(path = ":data:sub:sub1"),
            node(path = ":data:sub:sub2"),
          ),
        ),
      ),
    )
    val domainSubGraph = Subgraph(
      path = listOf("domain"),
      elements = mutableListOf(
        node(path = ":domain:a"),
        node(path = ":domain:b"),
      ),
    )
    val uiSubGraph = Subgraph(
      path = listOf("ui"),
      elements = mutableListOf(
        node(path = ":ui:a"),
        node(path = ":ui:b"),
        node(path = ":ui:c"),
      ),
    )

    val elements = buildGraphElements(
      typedModules = TwoLevelsOfSubmodules.modules,
      links = TwoLevelsOfSubmodules.links,
      thisPath = ":app",
    )
    assertThat(elements).isEqualTo(
      listOf(
        node(path = ":app"),
        dataSubGraph,
        domainSubGraph,
        uiSubGraph,
      ),
    )
  }

  @Test
  internal fun `Single nested module, no links, grouped`() {
    val node = node(
      path = ":a:b",
      type = ModuleType(name = "red", color = "red"),
    )
    val aGraph = Subgraph(
      path = listOf("a"),
      elements = listOf(node),
    )

    val elements = buildGraphElements(
      typedModules = SingleNestedModuleWithNoLinks.modules,
      links = SingleNestedModuleWithNoLinks.links,
      thisPath = ":a:b",
    )
    assertThat(elements).isEqualTo(listOf(aGraph))
  }
}
