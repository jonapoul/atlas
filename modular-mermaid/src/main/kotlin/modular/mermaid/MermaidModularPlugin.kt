/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid

import modular.core.ModularPlugin
import modular.core.internal.Variant.Chart
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.mermaid.internal.MermaidModularExtensionImpl
import modular.mermaid.tasks.WriteDummyMarkdownLegend
import modular.mermaid.tasks.WriteMarkdownLegend
import modular.mermaid.tasks.WriteMarkdownLegendBase
import modular.mermaid.tasks.WriteMermaidChart
import org.gradle.api.Project

class MermaidModularPlugin : ModularPlugin<MermaidModularExtensionImpl>(MermaidModularExtensionImpl::class) {
  override fun apply(target: Project) = with(target) {
    super.apply(target)

    afterEvaluate {
      // validation TBC
    }
  }

  override fun Project.registerChildTasks() {
    val spec = extension.mermaid

    val chartTask = WriteMermaidChart.register(
      target = project,
      name = WriteMermaidChart.TASK_NAME,
      extension = extension,
      spec = spec,
      outputFile = outputFile(extension, Chart, spec.fileExtension.get()),
    )

    val dummyChartTask = WriteMermaidChart.register(
      target = project,
      name = WriteMermaidChart.TASK_NAME_FOR_CHECKING,
      extension = extension,
      spec = spec,
      outputFile = modularBuildDirectory.get().file("chart-temp.mmd").asFile,
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
      legendTask = rootProject.tasks.named("writeMermaidLegend", WriteMarkdownLegend::class.java),
    )
  }

  override fun Project.registerRootTasks() {
    val spec = extension.mermaid

    val realTask = WriteMarkdownLegendBase.register<WriteMarkdownLegend>(
      target = project,
      spec = spec,
      variant = Legend,
      extension = extension,
      outputFile = outputFile(extension, Legend, fileExtension = "md"),
    )

    val dummyTask = WriteMarkdownLegendBase.register<WriteDummyMarkdownLegend>(
      target = project,
      spec = spec,
      variant = Legend,
      extension = extension,
      outputFile = modularBuildDirectory.get().file("legend-temp.md").asFile,
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
