/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import modular.core.internal.TypedModule
import modular.core.internal.readModuleType
import modular.test.ScenarioTest
import modular.test.androidHomeOrSkip
import modular.test.buildRunner
import modular.test.runTask
import modular.test.scenarios.ModuleTypesDeclaredButNoneMatch
import modular.test.scenarios.NoModuleTypesDeclared
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.ThreeModulesNoMatchingType
import modular.test.scenarios.ThreeModulesOnlyMatchingOther
import modular.test.scenarios.ThreeModulesWithCustomTypes
import modular.test.taskHadResult
import modular.test.taskWasSuccessful
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import java.io.File
import kotlin.test.Test

internal class WriteModuleTypeTest : ScenarioTest() {
  @Test
  internal fun `No module types declared`() = runScenario(NoModuleTypesDeclared) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleType("test-jvm")).isEqualTo(TypedModule(":test-jvm", type = null))
  }

  @Test
  internal fun `Module types declared but none match`() = runScenario(ModuleTypesDeclaredButNoneMatch) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleType("test-jvm")).isEqualTo(TypedModule(":test-jvm", type = null))
  }

  @Test
  internal fun `Write file if built-in type matches`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskWasSuccessful(":test-jvm:writeModuleType")
    assertThat(moduleType("test-jvm"))
      .isEqualTo(TypedModule(":test-jvm", type = ModuleType("Kotlin JVM", color = "mediumorchid")))

    // when running again, then it's cached
    val result2 = runTask("writeModuleType").build()
    assertThat(result2).taskHadResult(":test-jvm:writeModuleType", UP_TO_DATE)
  }

  @Test
  internal fun `Write files if custom types match`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = buildRunner(androidHomeOrSkip())
      .runTask("writeModuleType")
      .build()

    // then
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    assertThat(moduleType("test-data"))
      .isEqualTo(TypedModule(":test-data", type = ModuleType("Data", color = "#ABC123")))
    assertThat(moduleType("test-domain"))
      .isEqualTo(TypedModule(":test-domain", type = ModuleType("Domain", color = "#123ABC")))
    assertThat(moduleType("test-ui"))
      .isEqualTo(TypedModule(":test-ui", type = ModuleType("Android", color = "#A1B2C3")))
  }

  @Test
  internal fun `Fall back to other if no types match`() = runScenario(ThreeModulesOnlyMatchingOther) {
    // when
    runTask("writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(moduleType("a")).isEqualTo(TypedModule(":a", type = ModuleType("Other", color = "gainsboro")))
    assertThat(moduleType("b")).isEqualTo(TypedModule(":b", type = ModuleType("Other", color = "gainsboro")))
    assertThat(moduleType("c")).isEqualTo(TypedModule(":c", type = ModuleType("Other", color = "gainsboro")))
  }

  @Test
  internal fun `No types match`() = runScenario(ThreeModulesNoMatchingType) {
    // when
    val result = runTask("a:writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(result).taskHadResult(":a:writeModuleType", SUCCESS)
    assertThat(moduleType("a")).isEqualTo(TypedModule(":a", type = null))
  }

  private fun File.moduleType(modulePath: String) =
    resolve("$modulePath/build/modular/module-type.json")
      .let(::readModuleType)
}
