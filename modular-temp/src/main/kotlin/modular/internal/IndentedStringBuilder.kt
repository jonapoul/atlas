/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

internal class IndentedStringBuilder(private val indentSize: Int) {
  private val sb = StringBuilder()
  private var currentIndent = 0
  private var atLineStart = true
  private val indent get() = " ".repeat(currentIndent)

  fun appendLine(line: String = ""): IndentedStringBuilder {
    if (atLineStart) sb.append(indent)
    sb.appendLine(line)
    atLineStart = true
    return this
  }

  fun append(text: String): IndentedStringBuilder {
    if (atLineStart) sb.append(indent)
    sb.append(text)
    atLineStart = false
    return this
  }

  fun indent(block: IndentedStringBuilder.() -> Unit): IndentedStringBuilder {
    currentIndent += indentSize
    block()
    currentIndent -= indentSize
    return this
  }

  override fun toString() = sb.toString()
}

internal fun buildIndentedString(
  size: Int = 2,
  block: IndentedStringBuilder.() -> IndentedStringBuilder,
): String = IndentedStringBuilder(size)
  .block()
  .toString()
