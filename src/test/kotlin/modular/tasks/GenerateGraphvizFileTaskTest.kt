/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import modular.test.ModularTaskTest
import modular.test.RequiresGraphviz
import modular.test.runTask
import modular.test.scenarios.DotFileBasic
import modular.test.scenarios.DotFileBasicWithThreeOutputFormats
import kotlin.test.Test

class GenerateGraphvizFileTaskTest : ModularTaskTest() {
  @Test
  fun `Wrapper task has no dependents if no file formats have been declared`() = runScenario(DotFileBasic) {
    // when
    val result = runTask("generateModules", extras = listOf("--dry-run")).build()

    // then no PNGs, SVGs, or anything else were generated besides the dotfile
    assertThat(result.output).contains(
      """
        :a:calculateModuleTree SKIPPED
        :a:generateModulesDotFile SKIPPED
        :b:calculateModuleTree SKIPPED
        :b:generateModulesDotFile SKIPPED
        :c:calculateModuleTree SKIPPED
        :c:generateModulesDotFile SKIPPED

        BUILD SUCCESSFUL
      """.trimIndent(),
    )
  }

  @Test
  @RequiresGraphviz
  fun `Generate png, svg and eps files`() = runScenario(DotFileBasicWithThreeOutputFormats) {
    // when
    val result = runTask("generateModules").build()

    // then PNG, SVG and EPS tasks were run for each submodule
    listOf(
      ":a:generateModulesPng",
      ":a:generateModulesSvg",
      ":a:generateModulesEps",
      ":b:generateModulesPng",
      ":b:generateModulesSvg",
      ":b:generateModulesEps",
      ":c:generateModulesPng",
      ":c:generateModulesSvg",
      ":c:generateModulesEps",
    ).forEach {
      assertThat(result.output).contains(it)
    }

    // and the relevant files exist
    for (submodule in listOf("a", "b", "c")) {
      assertThat(resolve("$submodule/modules.png")).exists()
      assertThat(resolve("$submodule/modules.svg")).exists()
      assertThat(resolve("$submodule/modules.eps")).exists()
    }
  }
}
