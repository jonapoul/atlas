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
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.graphviz.internal.GraphvizModularExtensionImpl
import modular.graphviz.tasks.ExecGraphviz
import modular.graphviz.tasks.WriteGraphvizChart
import modular.graphviz.tasks.WriteGraphvizLegend
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
    val spec = extension.graphviz

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

  private fun Project.warnIfSvgSelectedWithCustomDpi() {
    val adjustSvgViewBox = extension.graphviz.adjustSvgViewBox.get()
    val warningIsSuppressed = extension.graphviz.properties.suppressSvgViewBoxWarning
    if (!adjustSvgViewBox && extension.graphviz.dpi.isPresent && !warningIsSuppressed.get()) {
      val msg = "Warning: Configuring a custom DPI with SVG output enabled will likely cause a misaligned " +
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
