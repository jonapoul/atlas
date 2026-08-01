package atlas.core.internal

import atlas.core.Framework
import atlas.core.Framework.D2
import atlas.core.Framework.Graphviz
import atlas.core.Framework.Mermaid
import atlas.core.tasks.TaskWithOutputFile
import atlas.d2.internal.D2Tasks
import atlas.graphviz.internal.GraphvizTasks
import atlas.mermaid.internal.MermaidTasks
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

/**
 * Task registration for a single [Framework]. One of these is installed per framework configured in
 * the `atlas { }` block.
 */
internal interface FrameworkTasks {
  val framework: Framework

  /** Tasks which only make sense once per build, e.g. the legend shared by every chart. */
  fun registerRootTasks(target: Project, extension: AtlasExtensionImpl)

  /** Tasks which generate this project's chart. */
  fun registerChildTasks(target: Project, extension: AtlasExtensionImpl): ChartFiles
}

/** The files a framework contributes to a project's README. */
internal data class ChartFiles(
  val framework: Framework,
  val chart: Provider<RegularFile>,
  val legend: Provider<out TaskWithOutputFile>?,
)

internal val Framework.tasks: FrameworkTasks
  get() =
    when (this) {
      D2 -> D2Tasks
      Graphviz -> GraphvizTasks
      Mermaid -> MermaidTasks
    }
