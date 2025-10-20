package atlas.graphviz.internal

import atlas.core.internal.IGradleProperties
import atlas.core.internal.enum
import atlas.graphviz.FileFormat
import atlas.graphviz.LayoutEngine
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val fileFormat: Provider<FileFormat> = enum("atlas.graphviz.fileFormat", default = FileFormat.Svg)
  val layoutEngine: Provider<LayoutEngine> = enum("atlas.graphviz.layoutEngine", default = null)
}
