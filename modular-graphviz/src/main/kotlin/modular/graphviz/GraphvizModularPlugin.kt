/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz

import modular.core.ModularPlugin
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.graphviz.internal.GraphvizModularExtensionImpl
import modular.graphviz.tasks.ExecGraphviz
import modular.graphviz.tasks.WriteDummyGraphvizChart
import modular.graphviz.tasks.WriteDummyGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizChart
import modular.graphviz.tasks.WriteGraphvizChartBase
import modular.graphviz.tasks.WriteGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizLegendBase
import org.gradle.api.Project

class GraphvizModularPlugin : ModularPlugin<GraphvizModularExtensionImpl>() {
  override fun Project.createExtension() = extensions.create(
    GraphvizModularExtension::class.java,
    ModularExtensionImpl.NAME,
    GraphvizModularExtensionImpl::class.java,
  ) as GraphvizModularExtensionImpl

  override fun Project.getExtension() =
    rootProject.extensions.getByType(GraphvizModularExtension::class.java) as GraphvizModularExtensionImpl

  override fun applyToRoot(target: Project) = with(target) {
    super.applyToRoot(target)

    afterEvaluate {
      warnIfSvgSelectedWithCustomDpi()
    }
  }

  override fun Project.registerChildTasks() {
    val graphvizSpec = extension.graphviz

    val chartTask = WriteGraphvizChartBase.register<WriteGraphvizChart>(
      target = project,
      extension = extension,
      spec = graphvizSpec,
      outputFile = outputFile(Chart, graphvizSpec.fileExtension.get()),
    )

    val dummyChartTask = WriteGraphvizChartBase.register<WriteDummyGraphvizChart>(
      target = project,
      extension = extension,
      spec = graphvizSpec,
      outputFile = modularBuildDirectory.get().file("modules-temp.dot").asFile,
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
    val spec = extension.graphviz

    val realTask = WriteGraphvizLegendBase.register<WriteGraphvizLegend>(
      target = project,
      spec = spec,
      extension = extension,
      outputFile = outputFile(Legend, spec.fileExtension.get()),
    )

    ExecGraphviz.register(
      target = project,
      spec = spec,
      variant = Legend,
      dotFileTask = realTask,
    )

    // Also validate the legend's dotfile when we call gradle check
    val dummyTask = WriteGraphvizLegendBase.register<WriteDummyGraphvizLegend>(
      target = project,
      spec = spec,
      extension = extension,
      outputFile = modularBuildDirectory.get().file("legend-temp.dot").asFile,
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

  private fun Project.warnIfSvgSelectedWithCustomDpi() {
    val adjustSvgViewBox = extension.graphviz.adjustSvgViewBox.get()
    val warningIsSuppressed = extension.properties.graphviz.suppressSvgViewBoxWarning
    if (!adjustSvgViewBox && extension.graphviz.dpi.isPresent && !warningIsSuppressed.get()) {
      val msg = "Configuring a custom DPI with SVG output enabled will likely cause a misaligned " +
        "viewBox. Try adding the following property to your build file to automatically attempt a fix:"
      logger.warn(
        """
        $msg

          modular {
            graphviz {
              adjustSvgViewBox = true
            }
          }

        or add "modular.graphviz.suppressAdjustSvgViewBox=true" to your gradle.properties file to suppress this warning.
        """.trimIndent(),
      )
    }
  }
}
