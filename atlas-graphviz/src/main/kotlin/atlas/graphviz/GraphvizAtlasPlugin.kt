/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package atlas.graphviz

import atlas.core.AtlasPlugin
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.tasks.CheckFileDiff
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

    afterEvaluate {
      // Validation TBC
    }
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

    WriteReadme.register(
      target = project,
      flavor = "Graphviz",
      chartFile = graphvizTask.map { it.outputFile.get() },
      legendTask = rootProject.tasks.named("execGraphvizLegend", ExecGraphviz::class.java),
    )
  }

  override fun Project.registerRootTasks() {
    val spec = graphVizExtension.graphviz

    val realTask = WriteGraphvizLegend.real(
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
