package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.TaskRegistrar
import modular.core.tasks.defaultOutputFile
import modular.graphviz.tasks.ExecGraphviz
import modular.graphviz.tasks.WriteDummyGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizLegendBase
import org.gradle.api.Project

internal object GraphvizTrunkTaskRegistrar : TaskRegistrar<GraphVizSpecImpl> {
  override fun invoke(
    target: Project,
    extension: ModularExtensionImpl,
    spec: GraphVizSpecImpl,
  ): Unit = with(target) {
    val realTask = WriteGraphvizLegendBase.register<WriteGraphvizLegend>(
      target = target,
      variant = Legend,
      spec = spec,
      extension = extension,
      outputFile = defaultOutputFile(extension, spec),
    )

    ExecGraphviz.register(
      target = target,
      extension = extension,
      spec = spec,
      variant = Legend,
      dotFileTask = realTask,
    )

    // Also validate the legend's dotfile when we call gradle check
    val dummyTask = WriteGraphvizLegendBase.register<WriteDummyGraphvizLegend>(
      target = target,
      variant = Legend,
      spec = spec,
      extension = extension,
      outputFile = modularBuildDirectory.map { it.file("legend-temp.dot") },
    )

    val checkLegend = CheckFileDiff.register(
      target = target,
      spec = spec,
      variant = Legend,
      realTask = realTask,
      dummyTask = dummyTask,
    )

    tasks.maybeCreate("check").dependsOn(checkLegend)
  }
}
