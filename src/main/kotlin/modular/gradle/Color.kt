/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

@JvmInline
value class Color private constructor(val hexValue: String) {
  init {
    require(REGEX_PATTERN.matches(hexValue)) {
      "Invalid color string '$hexValue' - should match regex pattern '$REGEX_PATTERN'"
    }
  }

  override fun toString(): String = hexValue

  companion object {
    private val REGEX_PATTERN = "^#[0-9A-Fa-f]{6}$".toRegex()
    private const val MAX_VALUE = 0xFFFFFF

    fun from(value: Int): Color = Color(hexValue = "#%06X".format(value))

    fun from(value: Long): Color {
      require(value <= MAX_VALUE) { "Invalid color integer $value" }
      return from(value.toInt())
    }

    fun from(value: String): Color {
      val hex = if (value.startsWith("#")) value else "#$value"
      return Color(hex)
    }
  }
}
