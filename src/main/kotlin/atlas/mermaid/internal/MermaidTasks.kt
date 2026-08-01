package atlas.mermaid.internal

import atlas.core.Framework
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.ChartFiles
import atlas.core.internal.FrameworkTasks
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.tasks.CheckFileDiff
import atlas.mermaid.tasks.WriteMarkdownLegend
import atlas.mermaid.tasks.WriteMermaidChart
import org.gradle.api.Project

internal object MermaidTasks : FrameworkTasks {
  override val framework: Framework = Framework.Mermaid

  override fun registerRootTasks(target: Project, extension: AtlasExtensionImpl): Unit =
    with(target) {
      val spec = extension.mermaid

      val realTask =
        WriteMarkdownLegend.real(
          target = project,
          extension = extension,
        )

      val dummyTask =
        WriteMarkdownLegend.dummy(
          target = project,
          extension = extension,
        )

      CheckFileDiff.register(
        target = project,
        extension = extension,
        variant = Legend,
        spec = spec,
        realTask = realTask,
        dummyTask = dummyTask,
      )
    }

  override fun registerChildTasks(target: Project, extension: AtlasExtensionImpl): ChartFiles =
    with(target) {
      val spec = extension.mermaid

      val chartTask =
        WriteMermaidChart.real(
          target = project,
          extension = extension,
          spec = spec,
        )

      val dummyChartTask =
        WriteMermaidChart.dummy(
          target = project,
          extension = extension,
          spec = spec,
        )

      CheckFileDiff.register(
        target = project,
        extension = extension,
        spec = spec,
        variant = Chart,
        realTask = chartTask,
        dummyTask = dummyChartTask,
      )

      ChartFiles(
        framework = framework,
        chart = chartTask.flatMap { it.outputFile },
        legend = WriteMarkdownLegend.get(rootProject),
      )
    }
}
