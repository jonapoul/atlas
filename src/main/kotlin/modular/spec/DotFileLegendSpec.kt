/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.provider.Property

interface DotFileLegendSpec {
  val cellBorder: Property<Int>
  val cellPadding: Property<Int>
  val cellSpacing: Property<Int>
  val tableBorder: Property<Int>
}
