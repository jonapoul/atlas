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
    assertThat(moduleLinks).isEmpty()
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
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
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
      setOf(
        ModuleLink(fromPath = ":a", toPath = ":b1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":a", toPath = ":b2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c1", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b1", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c2", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":b2", toPath = ":c3", configuration = "implementation", type = null),
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
    val customModuleLinksFileContents = readModuleLinks(resolve("custom-module-links-file.txt"))

    // and it contains the same from the diamond test a bit up from here
    assertThat(customModuleLinksFileContents).isEqualTo(
      setOf(
        ModuleLink(fromPath = ":mid-a", toPath = ":bottom", configuration = "api", type = null),
        ModuleLink(fromPath = ":mid-b", toPath = ":bottom", configuration = "implementation", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-a", configuration = "api", type = null),
        ModuleLink(fromPath = ":top", toPath = ":mid-b", configuration = "implementation", type = null),
      ),
    )
  }

  private val moduleLinksFile: File
    get() = projectRoot.resolve("build/modular/module-links")

  private val moduleLinks: Set<ModuleLink>
    get() = readModuleLinks(moduleLinksFile)
}
