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
  val center: Boolean? = null,
  val direction: Direction? = null,
  val groupLabelLocation: Location? = null,
  val groupLabelPosition: Position? = null,
  val layoutEngine: LayoutEngine? = null,
  val pad: Int? = null,
  val sketch: Boolean? = null,
  val rootStyle: Map<String, String>? = null,
  val globalProps: Map<String, String>? = null,
  val theme: Theme? = null,
  val darkTheme: Theme? = null,
) : JSerializable

internal fun D2Config(spec: D2Spec): D2Config = D2Config(
  animateLinks = spec.animateLinks.orNull,
  center = spec.center.orNull,
  direction = spec.direction.orNull,
  groupLabelLocation = spec.groupLabelLocation.orNull,
  groupLabelPosition = spec.groupLabelPosition.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  pad = spec.pad.orNull,
  sketch = spec.sketch.orNull,
  rootStyle = spec.rootStyle.properties.orNull,
  globalProps = spec.globalProps.properties.orNull,
  theme = spec.theme.orNull,
  darkTheme = spec.themeDark.orNull,
)
