/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import modular.core.internal.ModuleLink
import modular.core.internal.readModuleLinks
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.runTask
import modular.test.scenarios.DiamondGraph
import modular.test.scenarios.DiamondGraphWithUpwardsTraversal
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.TriangleGraph
import modular.test.scenarios.TriangleGraphWithUpwardsTraversal
import kotlin.test.Test

internal class WriteModuleTreeTest : ScenarioTest() {
  @Test
  internal fun `Empty files for single module with no dependencies`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("writeModuleTree")
      .withPluginClasspath()
      .build()

    // then
    assertThat(result.tasks).allSuccessful()

    // and the tree file is empty
    assertThat(moduleTree("test-jvm")).isEmpty()
  }

  @Test
  internal fun `Empty files for three modules with no dependencies`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("writeModuleTree").build()

    // then the tasks were run
    assertThat(result.tasks).allSuccessful()

    // and the tree files are empty
    assertThat(moduleTree("test-data")).isEmpty()
    assertThat(moduleTree("test-domain")).isEmpty()
    assertThat(moduleTree("test-ui")).isEmpty()
  }

  @Test
  internal fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("writeModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top module sees everything below it
    assertThat(moduleTree("top")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the mid only sees itself and bottom
    assertThat(moduleTree("mid-a")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
      ),
    )

    assertThat(moduleTree("mid-b")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
      ),
    )

    // and the bottom sees nothing
    assertThat(moduleTree("bottom")).isEmpty()
  }

  @Test
  internal fun `Single links for diamond with upwards traversal`() = runScenario(DiamondGraphWithUpwardsTraversal) {
    // when
    val result = runTask("writeModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top module sees everything below it
    assertThat(moduleTree("top")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the mid sees itself, top and bottom
    assertThat(moduleTree("mid-a")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
      ),
    )

    assertThat(moduleTree("mid-b")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )

    // and the bottom sees everything above it
    assertThat(moduleTree("bottom")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )
  }

  @Test
  internal fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("writeModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(moduleTree("a")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("b1")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("b2")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("c1")).isEmpty()
    assertThat(moduleTree("c2")).isEmpty()
    assertThat(moduleTree("c3")).isEmpty()
  }

  @Test
  internal fun `Multiple links for triangle with upwards traversal`() = runScenario(TriangleGraphWithUpwardsTraversal) {
    // when
    val result = runTask("writeModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(moduleTree("a")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("b1")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("b2")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("c1")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("c2")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
      ),
    )
    assertThat(moduleTree("c3")).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
      ),
    )
  }

  private fun moduleTree(module: String): Set<ModuleLink> = projectRoot
    .resolve("$module/build/modular/module-tree.json")
    .let(::readModuleLinks)
}
