/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz

import modular.core.ModularPlugin
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

class GraphvizModularPlugin : ModularPlugin<GraphvizModularExtensionImpl>(GraphvizModularExtensionImpl::class) {
  override fun apply(target: Project) = with(target) {
    super.apply(target)

    afterEvaluate {
      warnIfSvgSelectedWithCustomDpi()
    }
  }

  override fun Project.registerChildTasks() {
    val graphvizSpec = extension.graphviz

    val dotTask = WriteGraphvizChartBase.register<WriteGraphvizChart>(
      target = project,
      extension = extension,
      spec = graphvizSpec,
      variant = Chart,
      outputFile = outputFile(extension, Chart, graphvizSpec.fileExtension.get()),
      printOutput = true,
    )

    val dummyDotTask = WriteGraphvizChartBase.register<WriteDummyGraphvizChart>(
      target = project,
      extension = extension,
      spec = graphvizSpec,
      variant = Chart,
      outputFile = modularBuildDirectory.get().file("modules-temp.dot").asFile,
      printOutput = false,
    )

    CheckFileDiff.register(
      target = project,
      extension = extension,
      spec = graphvizSpec,
      variant = Chart,
      realTask = dotTask,
      dummyTask = dummyDotTask,
    )

    ExecGraphviz.register(
      target = project,
      spec = graphvizSpec,
      variant = Chart,
      dotFileTask = dotTask,
    )
  }

  override fun Project.registerRootTasks() {
    val spec = extension.graphviz

    val realTask = WriteGraphvizLegendBase.register<WriteGraphvizLegend>(
      target = project,
      variant = Legend,
      spec = spec,
      extension = extension,
      outputFile = outputFile(extension, Legend, spec.fileExtension.get()),
    )

    val graphvizTask = ExecGraphviz.register(
      target = project,
      spec = spec,
      variant = Legend,
      dotFileTask = realTask,
    )

    // Also validate the legend's dotfile when we call gradle check
    val dummyTask = WriteGraphvizLegendBase.register<WriteDummyGraphvizLegend>(
      target = project,
      variant = Legend,
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

    WriteReadme.register(
      target = project,
      flavor = "Graphviz",
      chartFile = graphvizTask.map { it.outputFile.get() },
      legendTask = rootProject.tasks.named("execGraphvizLegend", ExecGraphviz::class.java),
    )
  }

  private fun Project.warnIfSvgSelectedWithCustomDpi() {
    val adjustSvgViewBox = extension.graphviz.adjustSvgViewBox.get()
    val warningIsSuppressed = extension.properties.graphviz.suppressSvgViewBoxWarning
      .get()
    if (!adjustSvgViewBox && extension.graphviz.dpi.isPresent && !warningIsSuppressed) {
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
