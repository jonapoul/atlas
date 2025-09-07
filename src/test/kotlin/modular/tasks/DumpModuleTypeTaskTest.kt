/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import modular.test.ModularTaskTest
import modular.test.androidHomeOrSkip
import modular.test.buildRunner
import modular.test.runTask
import modular.test.scenarios.ModuleTypesDeclaredButNoneMatch
import modular.test.scenarios.NoModuleTypesDeclared
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.ThreeModulesWithCustomTypes
import modular.test.taskHadResult
import modular.test.taskWasSuccessful
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.Test

class DumpModuleTypeTaskTest : ModularTaskTest() {
  @Test
  fun `Fail if no module types declared`() = runScenario(NoModuleTypesDeclared) {
    // when
    val result = runTask("dumpModuleType").buildAndFail()

    // then
    assertThat(result.output).contains(
      """
        Execution failed for task ':test-jvm:dumpModuleType'.
        > No module type matching :test-jvm. All types = []
      """.trimIndent()
    )
  }

  @Test
  fun `Fail if module types declared but none match`() = runScenario(ModuleTypesDeclaredButNoneMatch) {
    // when
    val result = runTask("dumpModuleType").buildAndFail()

    // then
    assertThat(result.output).contains(
      """
        Execution failed for task ':test-jvm:dumpModuleType'.
        > No module type matching :test-jvm. All types = ["Android App", "Kotlin Multiplatform", "Android Library"]
      """.trimIndent()
    )
  }

  @Test
  fun `Write file if built-in type matches`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("dumpModuleType").build()

    // then
    assertThat(result).taskWasSuccessful(":test-jvm:dumpModuleType")
    val moduleTypeFile = resolve("test-jvm/build/reports/modular/module-type")
    assertThat(moduleTypeFile).exists()
    assertThat(moduleTypeFile("test-jvm")).isEqualTo(":test-jvm,Kotlin JVM,#CA66FF")

    // when running again, then it's cached
    val result2 = runTask("dumpModuleType").build()
    assertThat(result2).taskHadResult(":test-jvm:dumpModuleType", TaskOutcome.UP_TO_DATE)
  }

  @Test
  fun `Write files if custom types match`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = buildRunner(androidHomeOrSkip())
      .runTask("dumpModuleType")
      .build()

    // then
    assertThat(result).taskWasSuccessful(":test-data:dumpModuleType")
    assertThat(result).taskWasSuccessful(":test-domain:dumpModuleType")
    assertThat(result).taskWasSuccessful(":test-ui:dumpModuleType")

    assertThat(moduleTypeFile("test-data")).isEqualTo(":test-data,Data,#ABC123")
    assertThat(moduleTypeFile("test-domain")).isEqualTo(":test-domain,Domain,#123ABC")
    assertThat(moduleTypeFile("test-ui")).isEqualTo(":test-ui,Android,#A1B2C3")
  }

  private fun File.moduleTypeFile(modulePath: String): String =
    resolve("$modulePath/build/reports/modular/module-type").readText()
}
