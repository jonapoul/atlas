/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.d2

import modular.core.ModularDsl
import org.gradle.api.provider.MapProperty

@ModularDsl
interface D2StyleSpec {
  val properties: MapProperty<String, String>
  fun put(key: String, value: Any) = properties.put(key, value.toString())
}
