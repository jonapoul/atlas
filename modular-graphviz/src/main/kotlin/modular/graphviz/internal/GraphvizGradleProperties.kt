/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.enum
import modular.graphviz.FileFormat
import modular.graphviz.LayoutEngine
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val fileFormat: Provider<FileFormat> = enum("modular.graphviz.fileFormat", default = FileFormat.Svg)
  val layoutEngine: Provider<LayoutEngine> = enum("modular.graphviz.layoutEngine", default = null)
}
