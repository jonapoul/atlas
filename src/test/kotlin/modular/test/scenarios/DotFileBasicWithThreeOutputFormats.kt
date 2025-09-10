/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileBasicWithThreeOutputFormats : Scenario by DotFileBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      dotFile {
        fileFormats {
          svg()
          png()
          eps()
        }
      }
    }
  """.trimIndent()
}
