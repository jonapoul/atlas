/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object LeafUndeclaredOnChildrenWithAutoApply : Scenario by LeafUndeclaredOnChildren {
  override val gradlePropertiesFile = """
    modular.autoApplyToChildren=true
  """.trimIndent()
}
