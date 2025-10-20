@file:Suppress("unused") // public API

package atlas.mermaid

import atlas.core.AtlasPlugin
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.WriteReadme
import atlas.mermaid.internal.MermaidAtlasExtensionImpl
import atlas.mermaid.tasks.WriteMarkdownLegend
import atlas.mermaid.tasks.WriteMermaidChart
import org.gradle.api.Project

public class MermaidAtlasPlugin : AtlasPlugin() {
  private lateinit var mermaidExtension: MermaidAtlasExtensionImpl
  override val extension: AtlasExtensionImpl by lazy { mermaidExtension }

  override fun applyToRoot(target: Project): Unit = with(target) {
    mermaidExtension = extensions.create(
      MermaidAtlasExtension::class.java,
      AtlasExtensionImpl.NAME,
      MermaidAtlasExtensionImpl::class.java,
    ) as MermaidAtlasExtensionImpl

    super.applyToRoot(target)

    afterEvaluate {
      // Validation TBC
    }
  }

  override fun applyToChild(target: Project) {
    mermaidExtension = target.rootProject
      .extensions
      .getByType(MermaidAtlasExtension::class.java) as MermaidAtlasExtensionImpl

    super.applyToChild(target)
  }

  override fun Project.registerChildTasks() {
    val spec = mermaidExtension.mermaid

    val chartTask = WriteMermaidChart.real(
      target = project,
      extension = extension,
      spec = spec,
    )

    val dummyChartTask = WriteMermaidChart.dummy(
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

    WriteReadme.register(
      target = project,
      flavor = "Mermaid",
      chartFile = chartTask.map { it.outputFile.get() },
      legendTask = WriteMarkdownLegend.get(rootProject),
    )
  }

  override fun Project.registerRootTasks() {
    val spec = mermaidExtension.mermaid

    val realTask = WriteMarkdownLegend.real(
      target = project,
      extension = extension,
    )

    val dummyTask = WriteMarkdownLegend.dummy(
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
}
