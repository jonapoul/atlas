/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColorValidationTest {
  @Test
  fun `Throw for hex values too long`() {
    assertFalse("#ABC1234".isValid())
    assertFalse("#ABC1234".isValid())
    assertFalse("#ABC12345".isValid())
    assertFalse("#ABC123456".isValid())
    assertFalse("#ABC1234567".isValid())
    assertFalse("#ABC12345678".isValid())
    assertFalse("#ABC123456789".isValid())
  }

  @Test
  fun `Throw for hex values too short`() {
    assertFalse("#ABC12".isValid())
    assertFalse("#ABC1".isValid())
    assertFalse("#ABC".isValid())
    assertFalse("#AB".isValid())
    assertFalse("#A".isValid())
    assertFalse("#".isValid())
    assertFalse("".isValid())
  }

  @Test
  fun `Don't accept hex values without hash prefix`() {
    assertFalse("ABC123".isValid())
    assertFalse("000000".isValid())
    assertFalse("FFFFFF".isValid())
  }

  @Test
  fun `Valid colors`() {
    assertTrue("#ABC123".isValid())
    assertTrue("#000000".isValid())
    assertTrue("#FFFFFF".isValid())
  }

  private fun String.isValid() = matches(HEX_COLOR_REGEX)
}
