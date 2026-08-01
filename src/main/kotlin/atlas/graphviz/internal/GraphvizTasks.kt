package atlas.graphviz.internal

import atlas.core.Framework
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.ChartFiles
import atlas.core.internal.FrameworkTasks
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.tasks.CheckFileDiff
import atlas.graphviz.tasks.ExecGraphviz
import atlas.graphviz.tasks.WriteGraphvizChart
import atlas.graphviz.tasks.WriteGraphvizLegend
import org.gradle.api.Project

internal object GraphvizTasks : FrameworkTasks {
  override val framework: Framework = Framework.Graphviz

  override fun registerRootTasks(target: Project, extension: AtlasExtensionImpl): Unit =
    with(target) {
      val spec = extension.graphviz

      val realTask =
        WriteGraphvizLegend.real(
          target = project,
          spec = spec,
          extension = extension,
        )

      ExecGraphviz.register(
        target = project,
        spec = spec,
        variant = Legend,
        dotFileTask = realTask,
      )

      // Also validate the legend's dotfile when we call gradle check
      val dummyTask =
        WriteGraphvizLegend.dummy(
          target = project,
          spec = spec,
          extension = extension,
        )

      CheckFileDiff.register(
        target = project,
        extension = extension,
        spec = spec,
        variant = Legend,
        realTask = realTask,
        dummyTask = dummyTask,
      )
    }

  override fun registerChildTasks(target: Project, extension: AtlasExtensionImpl): ChartFiles =
    with(target) {
      val graphvizSpec = extension.graphviz

      val chartTask =
        WriteGraphvizChart.real(
          target = project,
          extension = extension,
          spec = graphvizSpec,
        )

      val dummyChartTask =
        WriteGraphvizChart.dummy(
          target = project,
          extension = extension,
          spec = graphvizSpec,
        )

      CheckFileDiff.register(
        target = project,
        extension = extension,
        spec = graphvizSpec,
        variant = Chart,
        realTask = chartTask,
        dummyTask = dummyChartTask,
      )

      val graphvizTask =
        ExecGraphviz.register(
          target = project,
          spec = graphvizSpec,
          variant = Chart,
          dotFileTask = chartTask,
        )

      ChartFiles(
        framework = framework,
        chart = graphvizTask.flatMap { it.outputFile },
        legend = rootProject.tasks.named("execGraphvizLegend", ExecGraphviz::class.java),
      )
    }
}
