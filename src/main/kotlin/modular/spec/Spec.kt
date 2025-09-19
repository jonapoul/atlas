/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.provider.Property

interface Spec<ChartSpec : Any> {
  // allows us store to store Spec in a NamedDomainObjectContainer
  val name: String
  val fileExtension: Property<String>

  val chart: ChartSpec
  @ModularDsl fun chart(action: Action<ChartSpec>) = action.execute(chart)
}
