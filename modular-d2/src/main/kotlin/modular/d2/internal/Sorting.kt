/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

internal fun <T> Map<String, T>.sortedByKeys(): List<Pair<String, T>> = toList().sortedBy { (k, _) -> k }
