/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import java.io.File
import kotlin.reflect.jvm.jvmName

interface OutputSpec<L : LegendSpec> {
  // allows us store to store OutputSpec in a NamedDomainObjectContainer
  val name get() = this::class.jvmName

  val legend: L
  fun legend(file: File) = legend.file.set(file)
  fun legend(file: Provider<RegularFile>) = legend.file.set(file)

  @ModularDsl
  fun legend(action: Action<L>) = action.execute(legend)
}
