/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid

import modular.core.ModularPlugin
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.mermaid.internal.MermaidModularExtensionImpl
import modular.mermaid.tasks.WriteDummyMarkdownLegend
import modular.mermaid.tasks.WriteDummyMermaidChart
import modular.mermaid.tasks.WriteMarkdownLegend
import modular.mermaid.tasks.WriteMarkdownLegendBase
import modular.mermaid.tasks.WriteMermaidChart
import modular.mermaid.tasks.WriteMermaidChartBase
import org.gradle.api.Project

class MermaidModularPlugin : ModularPlugin<MermaidModularExtensionImpl>() {
  override fun apply(target: Project) = with(target) {
    super.apply(target)

    afterEvaluate {
      // validation TBC
    }
  }

  override fun Project.createExtension() = extensions.create(
    MermaidModularExtension::class.java,
    ModularExtensionImpl.NAME,
    MermaidModularExtensionImpl::class.java,
  ) as MermaidModularExtensionImpl

  override fun Project.getExtension() =
    rootProject.extensions.getByType(MermaidModularExtension::class.java) as MermaidModularExtensionImpl

  override fun Project.registerChildTasks() {
    val spec = extension.mermaid

    val chartTask = WriteMermaidChartBase.register<WriteMermaidChart>(
      target = project,
      extension = extension,
      spec = spec,
      outputFile = outputFile(Chart, spec.fileExtension.get()),
    )

    val dummyChartTask = WriteMermaidChartBase.register<WriteDummyMermaidChart>(
      target = project,
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
      extension = extension,
      outputFile = outputFile(Legend, fileExtension = "md"),
    )

    val dummyTask = WriteMarkdownLegendBase.register<WriteDummyMarkdownLegend>(
      target = project,
      spec = spec,
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
