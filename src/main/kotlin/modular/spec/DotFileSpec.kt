/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.spec

import modular.gradle.ModularDsl
import modular.internal.string
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class DotFileSpec(
  private val objects: ObjectFactory,
  private val project: Project,
) : Spec<DotFileLegendSpec, DotFileChartSpec> {
  override val extension = objects.string(convention = "dot")

  override var legend: DotFileLegendSpec? = null
  override fun legend() = getOrBuildLegend()
  @ModularDsl override fun legend(action: Action<DotFileLegendSpec>) = action.execute(getOrBuildLegend())

  override val chart = DotFileChartSpec(objects, project)

  val fileFormats = DotFileOutputFormatSpec(objects)
  @ModularDsl fun fileFormats(action: Action<DotFileOutputFormatSpec>) = action.execute(fileFormats)

  private fun getOrBuildLegend() = legend ?: DotFileLegendSpec(objects, project).also { legend = it }
}
