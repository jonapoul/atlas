/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi

@InternalModularApi
public fun <T> Map<String, T>.sortedByKeys(): List<Pair<String, T>> = toList().sortedBy { (k, _) -> k }
