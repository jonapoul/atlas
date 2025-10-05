/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
class D2Config(
  val animateLinks: Boolean? = null,
  val arrowType: ArrowType? = null,
  val center: Boolean? = null,
  val direction: Direction? = null,
  val fontSize: Int? = null,
  val groupLabelLocation: Location? = null,
  val groupLabelPosition: Position? = null,
  val layoutEngine: LayoutEngine? = null,
  val pad: Int? = null,
  val sketch: Boolean? = null,
  val style: Map<String, String>? = null,
  val theme: Theme? = null,
  val darkTheme: Theme? = null,
) : JSerializable

internal fun D2Config(spec: D2Spec): D2Config = D2Config(
  animateLinks = spec.animateLinks.orNull,
  arrowType = spec.arrowType.orNull,
  center = spec.center.orNull,
  direction = spec.direction.orNull,
  fontSize = spec.fontSize.orNull,
  groupLabelLocation = spec.groupLabelLocation.orNull,
  groupLabelPosition = spec.groupLabelPosition.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  pad = spec.pad.orNull,
  sketch = spec.sketch.orNull,
  style = spec.rootStyle.properties.orNull,
  theme = spec.theme.orNull,
  darkTheme = spec.themeDark.orNull,
)
