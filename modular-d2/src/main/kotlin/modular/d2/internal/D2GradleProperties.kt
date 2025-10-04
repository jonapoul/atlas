/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.mapEnum
import modular.core.internal.string
import modular.d2.ArrowType
import modular.d2.Direction
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class D2GradleProperties(override val project: Project) : IGradleProperties {
  val arrowType: Provider<ArrowType> = string("modular.d2.arrowType", default = null).mapEnum()
  val containerLabelPosition: Provider<String> = string("modular.d2.containerLabelPosition", default = null)
  val direction: Provider<Direction> = string("modular.d2.direction", default = null).mapEnum()
}
