/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class ModularTaskTest {
  @TempDir lateinit var projectRoot: File

  @BeforeTest
  fun beforeModularTest() {
    basicSettingsFile().copyTo(projectRoot.resolve("settings.gradle.kts"))
  }

  protected fun <T> runScenario(scenario: Scenario, test: File.() -> T) {
    with(projectRoot) {
      resolve("settings.gradle.kts").writeText(scenario.settingsFile)
      resolve("build.gradle.kts").writeText(scenario.rootBuildFile)
      resolve("gradle.properties").writeText(scenario.gradlePropertiesFile)
      scenario.submoduleBuildFiles.forEach { (path, contents) ->
        resolve(path)
          .also { it.mkdirs() }
          .resolve("build.gradle.kts")
          .writeText(contents)
      }
      test()
    }
  }
}
