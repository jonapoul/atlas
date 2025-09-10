/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import java.io.File

class DotFileOutputSpec(
  private val objects: ObjectFactory,
  private val project: Project,
) : OutputSpec<DotFileLegendSpec, DotFileChartSpec> {
  override val chart = DotFileChartSpec(objects, project)

  override var legend: DotFileLegendSpec? = null
  override fun legend() = getOrBuildLegend()
  override fun legend(file: File) = getOrBuildLegend().also { it.file.set(file) }
  override fun legend(file: Provider<RegularFile>) = getOrBuildLegend().also { it.file.set(file) }

  @ModularDsl
  override fun legend(action: Action<DotFileLegendSpec>) = action.execute(getOrBuildLegend())

  private fun getOrBuildLegend() = legend ?: DotFileLegendSpec(objects, project).also { legend = it }
}
