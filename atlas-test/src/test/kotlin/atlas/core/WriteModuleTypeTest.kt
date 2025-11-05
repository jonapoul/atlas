package atlas.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import atlas.core.internal.TypedModule
import atlas.core.internal.readModuleType
import atlas.test.ScenarioTest
import atlas.test.androidHomeOrSkip
import atlas.test.buildRunner
import atlas.test.runTask
import atlas.test.scenarios.ModuleTypesDeclaredButNoneMatch
import atlas.test.scenarios.NoModuleTypesDeclared
import atlas.test.scenarios.OneKotlinJvmModule
import atlas.test.scenarios.ThreeModulesNoMatchingType
import atlas.test.scenarios.ThreeModulesOnlyMatchingOther
import atlas.test.scenarios.ThreeModulesWithCustomTypes
import atlas.test.taskHadResult
import atlas.test.taskWasSuccessful
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import java.io.File
import kotlin.test.Test

internal class WriteModuleTypeTest : ScenarioTest() {
  @Test
  fun `No module types declared`() = runScenario(NoModuleTypesDeclared) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleType("test-jvm")).isEqualTo(TypedModule(":test-jvm", type = null))
  }

  @Test
  fun `Module types declared but none match`() = runScenario(ModuleTypesDeclaredButNoneMatch) {
    // when
    val result = runTask("writeModuleType").build()

    // then
    assertThat(result).taskHadResult(":test-jvm:writeModuleType", SUCCESS)
    assertThat(moduleType("test-jvm")).isEqualTo(TypedModule(":test-jvm", type = null))
  }

  @Test
  fun `Write file if built-in type matches`() = runScenario(OneKotlinJvmModule) {
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
  fun `Write files if custom types match`() = runScenario(ThreeModulesWithCustomTypes) {
    // when
    val result = buildRunner(androidHomeOrSkip())
      .runTask("writeModuleType")
      .build()

    // then
    assertThat(result)
      .taskWasSuccessful(":test-data:writeModuleType")
      .taskWasSuccessful(":test-domain:writeModuleType")
      .taskWasSuccessful(":test-ui:writeModuleType")

    assertThat(moduleType("test-data"))
      .isEqualTo(TypedModule(":test-data", type = ModuleType("Data", color = "#ABC123")))
    assertThat(moduleType("test-domain"))
      .isEqualTo(TypedModule(":test-domain", type = ModuleType("Domain", color = "#123ABC")))
    assertThat(moduleType("test-ui"))
      .isEqualTo(TypedModule(":test-ui", type = ModuleType("Android", color = "#A1B2C3")))
  }

  @Test
  fun `Fall back to other if no types match`() = runScenario(ThreeModulesOnlyMatchingOther) {
    // when
    runTask("writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(moduleType("a")).isEqualTo(TypedModule(":a", type = ModuleType("Other", color = "gainsboro")))
    assertThat(moduleType("b")).isEqualTo(TypedModule(":b", type = ModuleType("Other", color = "gainsboro")))
    assertThat(moduleType("c")).isEqualTo(TypedModule(":c", type = ModuleType("Other", color = "gainsboro")))
  }

  @Test
  fun `No types match`() = runScenario(ThreeModulesNoMatchingType) {
    // when
    val result = runTask("a:writeModuleType", androidHomeOrSkip()).build()

    // then
    assertThat(result).taskHadResult(":a:writeModuleType", SUCCESS)
    assertThat(moduleType("a")).isEqualTo(TypedModule(":a", type = null))
  }

  private fun File.moduleType(modulePath: String) =
    resolve("$modulePath/build/atlas/module-type.json")
      .let(::readModuleType)
}
