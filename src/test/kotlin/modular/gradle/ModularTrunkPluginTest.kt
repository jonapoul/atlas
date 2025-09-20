/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import modular.test.ScenarioTest
import modular.test.runTask
import modular.test.scenarios.LeafDeclaredOnChildren
import modular.test.scenarios.LeafUndeclaredOnChildren
import modular.test.scenarios.LeafUndeclaredOnChildrenWithAutoApply
import kotlin.test.Test

class ModularTrunkPluginTest : ScenarioTest() {
  @Test
  fun `Apply to child projects when explicitly declared`() = runScenario(LeafDeclaredOnChildren) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then
    assertThat(result.output).contains(":a:modularGenerate SKIPPED")
    assertThat(result.output).contains(":b:modularGenerate SKIPPED")
    assertThat(result.output).contains(":c:modularGenerate SKIPPED")
  }

  @Test
  fun `Don't apply to child projects when not explicitly declared`() = runScenario(LeafUndeclaredOnChildren) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then
    assertThat(result.output).doesNotContain(":a:modularGenerate SKIPPED")
    assertThat(result.output).doesNotContain(":b:modularGenerate SKIPPED")
    assertThat(result.output).doesNotContain(":c:modularGenerate SKIPPED")
  }

  @Test
  fun `Auto-apply to child projects when gradle property set`() = runScenario(LeafUndeclaredOnChildrenWithAutoApply) {
    // when
    val result = runTask("modularGenerate", extras = listOf("--dry-run")).build()

    // then
    assertThat(result.output).contains(":a:modularGenerate SKIPPED")
    assertThat(result.output).contains(":b:modularGenerate SKIPPED")
    assertThat(result.output).contains(":c:modularGenerate SKIPPED")
  }
}
