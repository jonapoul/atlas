@file:Suppress("unused") // public API

package atlas.graphviz

import atlas.core.AtlasPlugin
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.SvgToPng
import atlas.core.tasks.WriteReadme
import atlas.graphviz.internal.GraphvizAtlasExtensionImpl
import atlas.graphviz.tasks.ExecGraphviz
import atlas.graphviz.tasks.WriteGraphvizChart
import atlas.graphviz.tasks.WriteGraphvizLegend
import org.gradle.api.Project

public class GraphvizAtlasPlugin : AtlasPlugin() {
  private lateinit var graphVizExtension: GraphvizAtlasExtensionImpl
  override val extension: AtlasExtensionImpl by lazy { graphVizExtension }

  override fun applyToRoot(target: Project): Unit = with(target) {
    graphVizExtension = extensions.create(
      GraphvizAtlasExtension::class.java,
      AtlasExtensionImpl.NAME,
      GraphvizAtlasExtensionImpl::class.java,
    ) as GraphvizAtlasExtensionImpl

    super.applyToRoot(target)
  }

  override fun applyToChild(target: Project) {
    graphVizExtension = target.rootProject
      .extensions
      .getByType(GraphvizAtlasExtension::class.java) as GraphvizAtlasExtensionImpl

    super.applyToChild(target)
  }

  override fun Project.registerChildTasks() {
    val graphvizSpec = graphVizExtension.graphviz

    val chartTask = WriteGraphvizChart.real(
      target = project,
      extension = extension,
      spec = graphvizSpec,
    )

    val dummyChartTask = WriteGraphvizChart.dummy(
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

    val graphvizTask = ExecGraphviz.register(
      target = project,
      spec = graphvizSpec,
      variant = Chart,
      dotFileTask = chartTask,
    )

    val svgToPng = SvgToPng.register(
      target = project,
      svgTask = graphvizTask,
      isSvgInput = graphvizSpec.fileFormat.map { it == FileFormat.Svg },
      converter = graphvizSpec.converter,
    )

    val taskForReadme = svgToPng.flatMap { task -> if (task.isEnabled) svgToPng else graphvizTask }

    val rootGraphviz = rootProject.tasks.named("execGraphvizLegend", ExecGraphviz::class.java)
    val rootSvgToPng = rootProject.tasks.named("svgToPng", SvgToPng::class.java)
    val legendForReadme = rootSvgToPng.flatMap { task -> if (task.isEnabled) rootSvgToPng else rootGraphviz }

    WriteReadme.register(
      target = project,
      flavor = "Graphviz",
      chartFile = taskForReadme.flatMap { it.outputFile },
      legendTask = legendForReadme,
    )
  }

  override fun Project.registerRootTasks() {
    val spec = graphVizExtension.graphviz

    val realTask = WriteGraphvizLegend.real(
      target = project,
      spec = spec,
      extension = extension,
    )

    val graphvizTask = ExecGraphviz.register(
      target = project,
      spec = spec,
      variant = Legend,
      dotFileTask = realTask,
    )

    SvgToPng.register(
      target = project,
      svgTask = graphvizTask,
      isSvgInput = spec.fileFormat.map { it == FileFormat.Svg },
      converter = spec.converter,
    )

    // Also validate the legend's dotfile when we call gradle check
    val dummyTask = WriteGraphvizLegend.dummy(
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
}
