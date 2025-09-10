/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.provider.Property
import kotlin.reflect.jvm.jvmName

sealed interface Spec<L : LegendSpec, C : ChartSpec> {
  // allows us store to store Spec in a NamedDomainObjectContainer
  val name get() = this::class.jvmName
  val extension: Property<String>

  var legend: L?
  fun legend(): L
  @ModularDsl fun legend(action: Action<L>)

  val chart: C
  @ModularDsl fun chart(action: Action<C>) = action.execute(chart)
}

interface ChartSpec {
  // TBC
}

interface LegendSpec {
  // TBC
}
