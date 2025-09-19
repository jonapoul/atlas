/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import modular.test.ScenarioTest
import modular.test.androidHomeOrSkip
import modular.test.buildRunner
import modular.test.contentEquals
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

class WriteModuleTypeTest : ScenarioTest() {
  @Test
  fun `No module types declared`() = runScenario(NoModuleTypesDeclared) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleTypeFile("test-jvm")).contentEquals(":test-jvm")
  }

  @Test
  fun `Module types declared but none match`() = runScenario(ModuleTypesDeclaredButNoneMatch) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleTypeFile("test-jvm")).contentEquals(":test-jvm")
  }

  @Test
  fun `Write file if built-in type matches`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskWasSuccessful(":test-jvm:writeModuleType")
    val moduleTypeFile = moduleTypeFile("test-jvm")
    assertThat(moduleTypeFile).exists()
    assertThat(moduleTypeFile.readText()).isEqualTo(":test-jvm,Kotlin JVM,#CA66FF")

    // when running again, then it's cached
    val result2 = runTask("writeModuleType").build()
    assertThat(result2).taskHadResult(":test-jvm:writeModuleType", UP_TO_DATE)
  }

  @Test
  fun `Write files if custom types match`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = buildRunner(androidHomeOrSkip())
      .runTask("writeModuleType")
      .build()

    // then
    assertThat(result).taskWasSuccessful(":test-data:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:writeModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:writeModuleType")

    assertThat(moduleTypeFileContents("test-data")).isEqualTo(":test-data,Data,#ABC123")
    assertThat(moduleTypeFileContents("test-domain")).isEqualTo(":test-domain,Domain,#123ABC")
    assertThat(moduleTypeFileContents("test-ui")).isEqualTo(":test-ui,Android,#A1B2C3")
  }

  @Test
  fun `Fall back to other if no types match`() = runScenario(ThreeModulesOnlyMatchingOther) {
    // when
    runTask("writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(moduleTypeFileContents("a")).isEqualTo(":a,Other,#808080")
    assertThat(moduleTypeFileContents("b")).isEqualTo(":b,Other,#808080")
    assertThat(moduleTypeFileContents("c")).isEqualTo(":c,Other,#808080")
  }

  @Test
  fun `No types match`() = runScenario(ThreeModulesNoMatchingType) {
    // when
    val result = runTask("a:writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(result).taskHadResult(":a:writeModuleType", SUCCESS)
    assertThat(moduleTypeFile("a")).contentEquals(":a")
  }

  private fun File.moduleTypeFile(modulePath: String): File = resolve("$modulePath/build/modular/module-type")

  private fun File.moduleTypeFileContents(modulePath: String): String = moduleTypeFile(modulePath).readText()
}
