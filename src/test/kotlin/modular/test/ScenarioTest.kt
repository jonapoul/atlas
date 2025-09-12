/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.junit.jupiter.api.io.TempDir
import java.io.File

@Suppress("UnnecessaryAbstractClass")
abstract class ScenarioTest {
  @TempDir lateinit var projectRoot: File

  protected fun <T> runScenario(scenario: Scenario, test: File.() -> T) {
    val settingsFile = """
      $REPOSITORIES_GRADLE_KTS
      ${scenario.submoduleBuildFiles.keys.joinToString(separator = "\n") { name -> "include(\":$name\")" }}
    """.trimIndent()

    with(projectRoot) {
      resolve("settings.gradle.kts").writeText(settingsFile)
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
