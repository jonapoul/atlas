/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.D2Scenario
import modular.test.KOTLIN_VERSION

internal object D2AllModuleTypes : D2Scenario by D2Basic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        androidApp()
        kotlinMultiplatform()
        androidLibrary()
        kotlinJvm()
        java()
        other()
      }
    }
  """.trimIndent()
}
