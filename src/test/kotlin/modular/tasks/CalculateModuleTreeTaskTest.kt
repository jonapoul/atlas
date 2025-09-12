/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
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

class CalculateModuleTreeTaskTest : ScenarioTest() {
  @Test
  fun `Empty files for single module with no dependencies`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then
    assertThat(result.tasks).allSuccessful()

    // and the tree file is empty
    assertThat(moduleTree("test-jvm")).isEmpty()
  }

  @Test
  fun `Empty files for three modules with no dependencies`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then the tasks were run
    assertThat(result.tasks).allSuccessful()

    // and the tree files are empty
    assertThat(moduleTree("test-data")).isEmpty()
    assertThat(moduleTree("test-domain")).isEmpty()
    assertThat(moduleTree("test-ui")).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top module sees everything below it
    assertThat(moduleTree("top")).isEqualTo(
      listOf(
        ":mid-a,:bottom,api",
        ":mid-b,:bottom,implementation",
        ":top,:mid-a,api",
        ":top,:mid-b,implementation",
      ),
    )

    // and the mid only sees itself and bottom
    assertThat(moduleTree("mid-a")).isEqualTo(listOf(":mid-a,:bottom,api"))
    assertThat(moduleTree("mid-b")).isEqualTo(listOf(":mid-b,:bottom,implementation"))

    // and the bottom sees nothing
    assertThat(moduleTree("bottom")).isEmpty()
  }

  @Test
  fun `Single links for diamond with upwards traversal`() = runScenario(DiamondGraphWithUpwardsTraversal) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the top module sees everything below it
    assertThat(moduleTree("top")).isEqualTo(
      listOf(
        ":mid-a,:bottom,api",
        ":mid-b,:bottom,implementation",
        ":top,:mid-a,api",
        ":top,:mid-b,implementation",
      ),
    )

    // and the mid sees itself, top and bottom
    assertThat(moduleTree("mid-a")).isEqualTo(
      listOf(
        ":mid-a,:bottom,api",
        ":top,:mid-a,api",
      ),
    )

    assertThat(moduleTree("mid-b")).isEqualTo(
      listOf(
        ":mid-b,:bottom,implementation",
        ":top,:mid-b,implementation",
      ),
    )

    // and the bottom sees everything above it
    assertThat(moduleTree("bottom")).isEqualTo(
      listOf(
        ":mid-a,:bottom,api",
        ":mid-b,:bottom,implementation",
        ":top,:mid-a,api",
        ":top,:mid-b,implementation",
      ),
    )
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(moduleTree("a")).isEqualTo(
      listOf(
        ":a,:b1,implementation",
        ":a,:b2,implementation",
        ":b1,:c1,implementation",
        ":b1,:c2,implementation",
        ":b2,:c2,implementation",
        ":b2,:c3,implementation",
      ),
    )
    assertThat(moduleTree("b1")).isEqualTo(
      listOf(
        ":b1,:c1,implementation",
        ":b1,:c2,implementation",
      ),
    )
    assertThat(moduleTree("b2")).isEqualTo(
      listOf(
        ":b2,:c2,implementation",
        ":b2,:c3,implementation",
      ),
    )
    assertThat(moduleTree("c1")).isEmpty()
    assertThat(moduleTree("c2")).isEmpty()
    assertThat(moduleTree("c3")).isEmpty()
  }

  @Test
  fun `Multiple links for triangle with upwards traversal`() = runScenario(TriangleGraphWithUpwardsTraversal) {
    // when
    val result = runTask("calculateModuleTree").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(moduleTree("a")).isEqualTo(
      listOf(
        ":a,:b1,implementation",
        ":a,:b2,implementation",
        ":b1,:c1,implementation",
        ":b1,:c2,implementation",
        ":b2,:c2,implementation",
        ":b2,:c3,implementation",
      ),
    )
    assertThat(moduleTree("b1")).isEqualTo(
      listOf(
        ":a,:b1,implementation",
        ":b1,:c1,implementation",
        ":b1,:c2,implementation",
      ),
    )
    assertThat(moduleTree("b2")).isEqualTo(
      listOf(
        ":a,:b2,implementation",
        ":b2,:c2,implementation",
        ":b2,:c3,implementation",
      ),
    )
    assertThat(moduleTree("c1")).isEqualTo(
      listOf(
        ":a,:b1,implementation",
        ":b1,:c1,implementation",
      ),
    )
    assertThat(moduleTree("c2")).isEqualTo(
      listOf(
        ":a,:b1,implementation",
        ":a,:b2,implementation",
        ":b1,:c2,implementation",
        ":b2,:c2,implementation",
      ),
    )
    assertThat(moduleTree("c3")).isEqualTo(
      listOf(
        ":a,:b2,implementation",
        ":b2,:c3,implementation",
      ),
    )
  }

  private fun moduleTree(module: String): List<String> = projectRoot
    .resolve("$module/build/modular/module-tree")
    .readLines()
    .filter { it.isNotBlank() }
}
