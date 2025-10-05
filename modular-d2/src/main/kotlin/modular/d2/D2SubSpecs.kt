/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.internal.PropertiesSpec

/**
 * https://d2lang.com/tour/style/#root
 */
@ModularDsl
interface D2RootStyleSpec : PropertiesSpec {
  var fill: String?
  var fillPattern: FillPattern?
  var stroke: String?
  var strokeWidth: Int?
  var strokeDash: Int?
  var doubleBorder: Boolean?
}

/**
 * Use this for any arbitrary global properties that you want to apply to all charts. These properties will be appended
 * to the bottom of the chart. Chances are, you'll want to use some [globs](https://d2lang.com/tour/globs/) in here,
 * so make sure to read the D2 docs on those.
 */
@ModularDsl
interface D2GlobalPropsSpec : PropertiesSpec {
  override fun put(key: String, value: Any)
  var arrowType: ArrowType?
  var fillArrowHeads: Boolean?
  var font: Font?
  var fontSize: Int?
}
