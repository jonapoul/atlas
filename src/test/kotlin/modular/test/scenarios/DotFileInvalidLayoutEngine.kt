/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileInvalidLayoutEngine : Scenario by DotFileBigGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      dotFile {
        chart {
          layoutEngine = "abc123"
        }

        fileFormats {
          svg()
        }
      }
    }
  """.trimIndent()
}
