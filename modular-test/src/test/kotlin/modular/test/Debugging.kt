/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused")

package modular.test

import java.io.File

fun File.allFilesSorted() = walkTopDown()
  .filter { it.isFile }
  .sorted()
  .toList()
