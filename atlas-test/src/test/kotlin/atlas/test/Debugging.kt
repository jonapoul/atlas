/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused")

package atlas.test

import java.io.File

internal fun File.allFilesSorted() = walkTopDown()
  .filter { it.isFile }
  .sorted()
  .toList()
