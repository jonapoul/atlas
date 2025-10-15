/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.D2Scenario

internal object D2ConfigureOnDemand : D2Scenario by D2Basic {
  override val gradlePropertiesFile = """
    org.gradle.configureondemand=true
  """.trimIndent()
}
