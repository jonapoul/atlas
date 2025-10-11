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
import modular.test.isEqualToSet
import modular.test.runTask
import modular.test.scenarios.CustomConfigurationExcluded
import modular.test.scenarios.CustomConfigurations
import modular.test.scenarios.DiamondGraph
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.TriangleGraph
import modular.test.taskWasSuccessful
import kotlin.test.Test

internal class WriteModuleLinksTest : ScenarioTest() {
  @Test
  fun `Empty file for single module with no dependencies`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-jvm:writeModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "test-jvm")).isEmpty()
  }

  @Test
  fun `Empty files for three modules with no dependencies`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-data:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "test-data")).isEmpty()
    assertThat(moduleLinks(module = "test-domain")).isEmpty()
    assertThat(moduleLinks(module = "test-ui")).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":top:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":mid-a:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":mid-b:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":bottom:writeModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks(module = "top")).isEqualToSet(
      ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
      ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
    )
    assertThat(moduleLinks(module = "mid-a")).isEqualToSet(
      ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
    )
    assertThat(moduleLinks(module = "mid-b")).isEqualToSet(
      ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
    )
    assertThat(moduleLinks(module = "bottom")).isEqualTo(emptySet())
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected as expected
    assertThat(moduleLinks(module = "a")).isEqualToSet(
      ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
      ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
    )
    assertThat(moduleLinks(module = "b1")).isEqualToSet(
      ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
      ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
    )
    assertThat(moduleLinks(module = "b2")).isEqualToSet(
      ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
      ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
    )
    assertThat(moduleLinks(module = "c1")).isEmpty()
    assertThat(moduleLinks(module = "c2")).isEmpty()
    assertThat(moduleLinks(module = "c3")).isEmpty()
  }

  @Test
  fun `Custom configuration is picked up if we dont exclude it`() = runScenario(CustomConfigurations) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links
    assertThat(moduleLinks(module = "a")).isEqualToSet(
      ModuleLink(fromPath = ":a", toPath = ":b", configuration = "abc", type = null),
      ModuleLink(fromPath = ":a", toPath = ":b", configuration = "xyz", type = null),
    )
    assertThat(moduleLinks(module = "b")).isEmpty()
  }

  @Test
  fun `Custom configuration is excluded`() = runScenario(CustomConfigurationExcluded) {
    // when
    val result = runTask("writeModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the two custom configs were detected as links, but the xyz config was excluded
    assertThat(moduleLinks(module = "a")).isEqualToSet(
      ModuleLink(fromPath = ":a", toPath = ":b", configuration = "abc", type = null),
    )
    assertThat(moduleLinks(module = "b")).isEmpty()
  }

  private fun moduleLinks(module: String): Set<ModuleLink> = projectRoot
    .resolve("$module/build/modular/module-links.json")
    .let(::readModuleLinks)
}
