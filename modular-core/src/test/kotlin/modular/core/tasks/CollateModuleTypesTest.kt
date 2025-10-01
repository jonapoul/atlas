/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import assertk.assertThat
import assertk.assertions.isEqualTo
import modular.test.ScenarioTest
import modular.test.contentEquals
import modular.test.runTask
import modular.test.scenarios.NoSubmodules
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.ThreeModulesWithCustomTypes
import modular.test.taskWasSuccessful
import java.io.File
import kotlin.test.Test

class CollateModuleTypesTest : ScenarioTest() {
  @Test
  fun `Collate three custom types`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then three dependent tasks were run
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateModuleTypes")

    // and the types were aggregated in the root module's build dir
    assertThat(moduleTypesFile).contentEquals(
      expected = """
        :test-data,Data,#ABC123
        :test-domain,Domain,#123ABC
        :test-ui,Android,#A1B2C3

      """.trimIndent(),
    )
  }

  @Test
  fun `Collate three built in types`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then three dependent tasks were run
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    // and this one
    assertThat(result).taskWasSuccessful(":collateModuleTypes")

    // and the types were aggregated in the root module's build dir
    assertThat(moduleTypesFile).contentEquals(
      expected = """
        :test-data,Java,orange
        :test-domain,Kotlin JVM,mediumorchid
        :test-ui,Android Library,lightgreen

      """.trimIndent(),
    )
  }

  @Test
  fun `Collate with no submodules`() = runScenario(NoSubmodules) {
    // when
    val result = runTask("collateModuleTypes").build()

    // then no write tasks were run
    val taskPaths = result.tasks.map { it.path }
    assertThat(taskPaths).isEqualTo(listOf(":collateModuleTypes"))

    // and no types were collated, but the task still passed
    assertThat(result).taskWasSuccessful(":collateModuleTypes")
    assertThat(moduleTypesFile).contentEquals(expected = "")
  }

  private val File.moduleTypesFile get() = resolve("build/modular/module-types")
}
