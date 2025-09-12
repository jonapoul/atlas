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
import modular.test.scenarios.CustomConfigurationExcluded
import modular.test.scenarios.CustomConfigurations
import modular.test.scenarios.DiamondGraph
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.TriangleGraph
import modular.test.taskWasSuccessful
import kotlin.test.Test

class DumpModuleLinksTaskTest : ScenarioTest() {
  @Test
  fun `Empty file for single module with no dependencies`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-jvm:dumpModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "test-jvm")).isEqualTo(emptyList())
  }

  @Test
  fun `Empty files for three modules with no dependencies`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-data:dumpModuleLinks")
    assertThat(result).taskWasSuccessful(":test-domain:dumpModuleLinks")
    assertThat(result).taskWasSuccessful(":test-ui:dumpModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "test-data")).isEmpty()
    assertThat(moduleLinks(module = "test-domain")).isEmpty()
    assertThat(moduleLinks(module = "test-ui")).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":top:dumpModuleLinks")
    assertThat(result).taskWasSuccessful(":mid-a:dumpModuleLinks")
    assertThat(result).taskWasSuccessful(":mid-b:dumpModuleLinks")
    assertThat(result).taskWasSuccessful(":bottom:dumpModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "top"))
      .isEqualTo(listOf(":top,:mid-a,api", ":top,:mid-b,implementation"))
    assertThat(moduleLinks(module = "mid-a"))
      .isEqualTo(listOf(":mid-a,:bottom,api"))
    assertThat(moduleLinks(module = "mid-b"))
      .isEqualTo(listOf(":mid-b,:bottom,implementation"))
    assertThat(moduleLinks(module = "bottom"))
      .isEqualTo(emptyList())
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected as expected
    assertThat(moduleLinks(module = "a")).isEqualTo(listOf(":a,:b1,implementation", ":a,:b2,implementation"))
    assertThat(moduleLinks(module = "b1")).isEqualTo(listOf(":b1,:c1,implementation", ":b1,:c2,implementation"))
    assertThat(moduleLinks(module = "b2")).isEqualTo(listOf(":b2,:c2,implementation", ":b2,:c3,implementation"))
    assertThat(moduleLinks(module = "c1")).isEmpty()
    assertThat(moduleLinks(module = "c2")).isEmpty()
    assertThat(moduleLinks(module = "c3")).isEmpty()
  }

  @Test
  fun `Custom configuration is picked up if we dont exclude it`() = runScenario(CustomConfigurations) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links
    assertThat(moduleLinks(module = "a")).isEqualTo(listOf(":a,:b,abc", ":a,:b,xyz"))
    assertThat(moduleLinks(module = "b")).isEmpty()
  }

  @Test
  fun `Custom configuration is excluded`() = runScenario(CustomConfigurationExcluded) {
    // when
    val result = runTask("dumpModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links, but the xyz config was excluded
    assertThat(moduleLinks(module = "a")).isEqualTo(listOf(":a,:b,abc"))
    assertThat(moduleLinks(module = "b")).isEmpty()
  }

  private fun moduleLinks(module: String) = projectRoot
    .resolve("$module/build/modular/module-links")
    .readLines()
    .filter { it.isNotBlank() }
}
