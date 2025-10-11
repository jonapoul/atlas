/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.javaBuildScript

internal object PathMatches : GraphvizScenario {
  const val THREE_QUOTES = "\"\"\""

  override val rootBuildFile = """
    import kotlin.text.RegexOption.*

    plugins {
      `java`
      id("$pluginId")
    }

    modular {
      moduleTypes {
        registerByPathMatches(
          name = "A",
          color = "orange",
          pathMatches = $THREE_QUOTES.*[a-z]+\d+$THREE_QUOTES,
        )

        registerByPathMatches(
          name = "B",
          color = "limegreen",
          pathMatches = $THREE_QUOTES.*[a-z]+-[a-z]$THREE_QUOTES,
          options = setOf(IGNORE_CASE),
        )

        registerByPathMatches(
          name = "C",
          color = "mediumslateblue",
          pathMatches = $THREE_QUOTES^:HELLO$$THREE_QUOTES,
          options = setOf(IGNORE_CASE),
        )

        registerByPathMatches(
          name = "D",
          color = "gainsboro",
          pathMatches = $THREE_QUOTES:[a-z]\d-.*$THREE_QUOTES,
          options = setOf(IGNORE_CASE),
        )

        registerByPathMatches(
          name = "E",
          color = "mediumorchid",
          pathMatches = $THREE_QUOTES.*\w+-\w+$THREE_QUOTES,
          options = setOf(DOT_MATCHES_ALL),
        )
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "abc123" to javaBuildScript,
    "Test-X" to javaBuildScript,
    "hello" to javaBuildScript,
    "a1-B2-C3" to javaBuildScript,
    "foo-bar" to javaBuildScript,
  )
}
