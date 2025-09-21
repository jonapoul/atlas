package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.TaskRegistrar
import modular.core.tasks.WriteReadme
import modular.graphviz.tasks.ExecGraphviz
import modular.graphviz.tasks.WriteGraphvizChart
import org.gradle.api.Project

internal object GraphvizLeafTaskRegistrar : TaskRegistrar<GraphVizSpecImpl> {
  override fun invoke(
    target: Project,
    extension: ModularExtensionImpl,
    spec: GraphVizSpecImpl,
  ): Unit = with(target) {
    val dotFileTask = WriteGraphvizChart.register(
      target = this,
      name = WriteGraphvizChart.TASK_NAME,
      extension = extension,
      spec = spec,
      outputFile = file,
      printOutput = true,
    )

    val tempDotFileTask = WriteGraphvizChart.register(
      target = this,
      name = WriteGraphvizChart.TASK_NAME_FOR_CHECKING,
      extension = extension,
      spec = spec,
      outputFile = modularBuildDirectory.get().file("modules-temp.dot"),
      printOutput = false,
    )

    val checkTask = CheckFileDiff.register(
      target = this,
      spec = spec,
      variant = Legend,
      generateTask = tempDotFileTask,
      realFile = file,
    )

    tasks.maybeCreate("check").dependsOn(checkTask)

    val graphvizTask = ExecGraphviz.register(
      target = this,
      extension = extension,
      spec = spec,
      variant = Variant.Chart,
      dotFileTask = dotFileTask,
    )

    WriteReadme.register(
      target = this,
      enabled = spec.writeReadme,
      flavor = "Graphviz",
      chartFile = graphvizTask.map { it.outputFile.get() },
      legendTask = rootProject.tasks.named("generateGraphvizLegend", ExecGraphviz::class.java),
    )
  }
}
