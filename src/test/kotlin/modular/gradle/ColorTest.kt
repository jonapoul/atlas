/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ColorTest {
  @Test
  fun `Throw for value too big`() {
    assertFailsWith<IllegalArgumentException> { Color.from(0xFFFFFF + 1) }
    assertFailsWith<IllegalArgumentException> { Color.from(0xFFFFFF * 2) }
    assertFailsWith<IllegalArgumentException> { Color.from(Long.MAX_VALUE) }
  }

  @Test
  fun `Throw for hex values too long`() {
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC1234") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC12345") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC123456") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC1234567") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC12345678") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC123456789") }
  }

  @Test
  fun `Throw for hex values too short`() {
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC12") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC1") }
    assertFailsWith<IllegalArgumentException> { Color.from("#ABC") }
    assertFailsWith<IllegalArgumentException> { Color.from("#AB") }
    assertFailsWith<IllegalArgumentException> { Color.from("#A") }
    assertFailsWith<IllegalArgumentException> { Color.from("#") }
    assertFailsWith<IllegalArgumentException> { Color.from("") }
  }

  @Test
  fun `Accept hex values without hash prefix`() {
    assertDoesNotThrow { Color.from("ABC123") }
    assertDoesNotThrow { Color.from("000000") }
    assertDoesNotThrow { Color.from("FFFFFF") }
  }

  @Test
  fun `String vs int equivalence`() {
    assertEquals(Color.from(0x000000), Color.from("#000000"))
    assertEquals(Color.from(0xFFFFFF), Color.from("#FFFFFF"))
    assertEquals(Color.from(0xABC123), Color.from("#ABC123"))
  }

  @Test
  fun `Should pad with leading zeros`() {
    assertEquals("#000001", Color.from(1).hexValue)
    assertEquals("#0000FF", Color.from(255).hexValue)
    assertEquals("#001234", Color.from(0x1234).hexValue)
  }
}
