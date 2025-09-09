/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class DotFileOutputSpec(objects: ObjectFactory, project: Project) : OutputSpec<DotFileLegendSpec, DotFileChartSpec> {
  override val legend = DotFileLegendSpec(objects, project)
  override val chart = DotFileChartSpec(objects, project)
}
