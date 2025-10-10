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
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.mermaid.internal.MermaidModularExtensionImpl
import modular.mermaid.tasks.WriteMarkdownLegend
import modular.mermaid.tasks.WriteMermaidChart
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
    val spec = extension.mermaid

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
