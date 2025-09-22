/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.mermaid.spec.MermaidSpec
import modular.mermaid.tasks.WriteDummyMarkdownLegend
import modular.mermaid.tasks.WriteMarkdownLegend
import modular.mermaid.tasks.WriteMarkdownLegendBase
import modular.mermaid.tasks.WriteMermaidChart
import org.gradle.api.Project

internal fun Project.registerMermaidTrunkTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpecImpl,
) {
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
    variant = Legend,
    spec = spec,
    realTask = realTask,
    dummyTask = dummyTask,
  )
}

internal fun Project.registerMermaidLeafTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpec,
) {
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
    spec = spec,
    variant = Chart,
    realTask = chartTask,
    dummyTask = dummyChartTask,
  )

  WriteReadme.register(
    target = project,
    enabled = spec.writeReadme,
    flavor = "Mermaid",
    chartFile = chartTask.map { it.outputFile.get() },
    legendTask = rootProject.tasks.named("writeMermaidLegend", WriteMarkdownLegend::class.java),
  )
}
