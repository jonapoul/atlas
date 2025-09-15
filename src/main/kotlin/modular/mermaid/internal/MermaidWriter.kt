/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.buildIndentedString

internal class MermaidWriter {
  operator fun invoke(): String = buildIndentedString(size = 2) {
    appendLine("TBC")
  }
}
