/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import modular.test.ScenarioTest
import modular.test.allSuccessful
import modular.test.doesNotExist
import modular.test.runTask
import modular.test.scenarios.DiamondGraph
import modular.test.scenarios.OneKotlinJvmModule
import modular.test.scenarios.OverrideModuleLinksFile
import modular.test.scenarios.ThreeModulesWithBuiltInTypes
import modular.test.scenarios.TriangleGraph
import modular.test.taskWasSuccessful
import java.io.File
import kotlin.test.Test

class CollateModuleLinksTest : ScenarioTest() {
  @Test
  fun `Empty file for single module with no dependencies`() = runScenario(OneKotlinJvmModule) {
    // when
    val result = runTask("collateModuleLinks").build()

    // then the task was run
    assertThat(result).taskWasSuccessful(":test-jvm:writeModuleLinks")
    assertThat(result).taskWasSuccessful(":collateModuleLinks")

    // and the links file is empty
    assertThat(moduleLinks).isEqualTo(emptyList())
  }

  @Test
  fun `Empty file for three modules with no dependencies`() = runScenario(ThreeModulesWithBuiltInTypes) {
    // when
    val result = runTask("collateModuleLinks").build()

    // then the tasks were run
    assertThat(result.tasks).allSuccessful()

    // and the links file is empty
    assertThat(moduleLinks).isEmpty()
  }

  @Test
  fun `Single links for diamond`() = runScenario(DiamondGraph) {
    // when
    val result = runTask("collateModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the links file contains the expected, in a-z order
    assertThat(moduleLinks).isEqualTo(
      listOf(
        ":mid-a,:bottom,api,,",
        ":mid-b,:bottom,implementation,,",
        ":top,:mid-a,api,,",
        ":top,:mid-b,implementation,,",
      ),
    )
  }

  @Test
  fun `Multiple links for triangle`() = runScenario(TriangleGraph) {
    // when
    val result = runTask("collateModuleLinks").build()

    // then the task was run
    assertThat(result.tasks).allSuccessful()

    // and the triangle links were detected, in a-z order
    assertThat(moduleLinks).isEqualTo(
      listOf(
        ":a,:b1,implementation,,",
        ":a,:b2,implementation,,",
        ":b1,:c1,implementation,,",
        ":b1,:c2,implementation,,",
        ":b2,:c2,implementation,,",
        ":b2,:c3,implementation,,",
      ),
    )
  }

  @Test
  fun `Can override task conventions from build script`() = runScenario(OverrideModuleLinksFile) {
    // when
    runTask("collateModuleLinks").build()

    // then the default config file wasn't created
    assertThat(moduleLinksFile).doesNotExist()

    // but the custom one does
    val customModuleLinksFileContents = resolve("custom-module-links-file.txt")
      .readLines()
      .filter { it.isNotBlank() }

    // and it contains the same from the diamond test a bit up from here
    assertThat(customModuleLinksFileContents).isEqualTo(
      listOf(
        ":mid-a,:bottom,api,,",
        ":mid-b,:bottom,implementation,,",
        ":top,:mid-a,api,,",
        ":top,:mid-b,implementation,,",
      ),
    )
  }

  private val moduleLinksFile: File
    get() = projectRoot.resolve("build/modular/module-links")

  private val moduleLinks: List<String>
    get() = moduleLinksFile
      .readLines()
      .filter { it.isNotBlank() }
}
