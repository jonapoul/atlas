package atlas.mermaid.internal

import atlas.core.Framework
import atlas.core.internal.AtlasArtifact
import atlas.core.internal.AtlasContext
import atlas.core.internal.ChartFiles
import atlas.core.internal.FrameworkTasks
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.internal.publishAtlasArtifact
import atlas.core.tasks.CheckFileDiff
import atlas.mermaid.tasks.WriteMarkdownLegend
import atlas.mermaid.tasks.WriteMermaidChart

internal object MermaidTasks : FrameworkTasks {
  override val framework: Framework = Framework.Mermaid

  override fun registerRootTasks(context: AtlasContext): Unit =
    with(context.project) {
      val spec = context.mermaid

      val realTask = WriteMarkdownLegend.real(context)

      publishAtlasArtifact(
        artifact = AtlasArtifact.legend(framework),
        file = realTask.flatMap { it.outputFile },
        builtBy = realTask,
      )

      val dummyTask = WriteMarkdownLegend.dummy(context)

      CheckFileDiff.register(
        target = this,
        config = context.config,
        variant = Legend,
        spec = spec,
        realTask = realTask,
        dummyTask = dummyTask,
      )
    }

  override fun registerChildTasks(context: AtlasContext): ChartFiles =
    with(context.project) {
      val spec = context.mermaid

      val chartTask = WriteMermaidChart.real(context = context, spec = spec)
      val dummyChartTask = WriteMermaidChart.dummy(context = context, spec = spec)

      CheckFileDiff.register(
        target = this,
        config = context.config,
        spec = spec,
        variant = Chart,
        realTask = chartTask,
        dummyTask = dummyChartTask,
      )

      ChartFiles(
        framework = framework,
        chart = chartTask.flatMap { it.outputFile },
        legend = context.fromRoot(AtlasArtifact.legend(framework)),
      )
    }
}
