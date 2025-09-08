/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory

class DotFileOutputSpec(objects: ObjectFactory, project: Project) : OutputSpec<DotFileLegendSpec> {
  override val legend = DotFileLegendSpec(objects, project)

  val chartDotFile: RegularFileProperty = objects
    .fileProperty()
    .convention(
      project.rootProject
        .layout
        .projectDirectory
        .file("modules.dot"),
    )
}
