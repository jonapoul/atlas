/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import modular.core.internal.TypedModule
import modular.core.internal.readModuleTypes
import modular.test.ScenarioTest
import modular.test.isEqualToSet
import modular.test.runTask
import modular.test.scenarios.NoSubmodules
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.ThreeModulesWithCustomTypes
import modular.test.taskWasSuccessful
import java.io.File
import kotlin.test.Test

internal class CollateModuleTypesTest : ScenarioTest() {
  @Test
  internal fun `Collate three custom types`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then three dependent tasks were run
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateModuleTypes")

    // and the types were aggregated in the root module's build dir
    assertThat(moduleTypes).isEqualToSet(
      TypedModule(projectPath = ":test-data", type = ModuleType(name = "Data", color = "#ABC123")),
      TypedModule(projectPath = ":test-domain", type = ModuleType(name = "Domain", color = "#123ABC")),
      TypedModule(projectPath = ":test-ui", type = ModuleType(name = "Android", color = "#A1B2C3")),
    )
  }

  @Test
  internal fun `Collate three built in types`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then three dependent tasks were run
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateModuleTypes")

    // and the types were aggregated in the root module's build dir
    assertThat(moduleTypes).isEqualToSet(
      TypedModule(projectPath = ":test-data", type = ModuleType(name = "Java", color = "orange")),
      TypedModule(projectPath = ":test-domain", type = ModuleType(name = "Kotlin JVM", color = "mediumorchid")),
      TypedModule(projectPath = ":test-ui", type = ModuleType(name = "Android Library", color = "lightgreen")),
    )
  }

  @Test
  internal fun `Collate with no submodules`() = runScenario(NoSubmodules) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then no write tasks were run
    val taskPaths = result.tasks.map { it.path }
    assertThat(taskPaths).isEqualTo(listOf(":collateModuleTypes"))

    // and no types were collated, but the task still passed
    assertThat(result).taskWasSuccessful(":collateModuleTypes")
    assertThat(moduleTypes).isEmpty()
  }

  private val File.moduleTypes get() = resolve("build/modular/module-types.json").let(::readModuleTypes)
}
