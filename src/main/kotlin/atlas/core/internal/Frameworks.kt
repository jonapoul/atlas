package atlas.core.internal

import atlas.core.Framework
import atlas.core.Framework.D2
import atlas.core.Framework.Graphviz
import atlas.core.Framework.Mermaid
import atlas.d2.internal.D2Tasks
import atlas.graphviz.internal.GraphvizTasks
import atlas.mermaid.internal.MermaidTasks
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

/**
 * Task registration for a single [Framework]. One of these is installed per framework configured in
 * the `atlas { }` block.
 */
internal interface FrameworkTasks {
  val framework: Framework

  /** Tasks which only make sense once per build, e.g. the legend shared by every chart. */
  fun registerRootTasks(context: AtlasContext)

  /** Tasks which generate this project's chart. */
  fun registerChildTasks(context: AtlasContext): ChartFiles
}

/**
 * The files a framework contributes to a project's README. The legend is drawn on the root project,
 * so it arrives as a resolved artifact rather than as a task reference.
 */
internal data class ChartFiles(
  val framework: Framework,
  val chart: Provider<RegularFile>,
  val legend: FileCollection?,
)

internal val Framework.tasks: FrameworkTasks
  get() =
    when (this) {
      D2 -> D2Tasks
      Graphviz -> GraphvizTasks
      Mermaid -> MermaidTasks
    }
