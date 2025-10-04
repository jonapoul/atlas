/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.d2

import modular.core.ModularDsl
import modular.core.internal.PropertiesSpec

/**
 * https://d2lang.com/tour/style/
 */
@ModularDsl
interface D2StyleSpec : PropertiesSpec

/**
 * https://d2lang.com/tour/style/#root
 */
@ModularDsl
interface D2RootStyleSpec : D2StyleSpec {
  var fill: String?
  var fillPattern: FillPattern?
  var stroke: String?
  var strokeWidth: Int?
  var strokeDash: Int?
  var doubleBorder: Int?
}
