/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.core.tasks.defaultOutputFile
import modular.mermaid.spec.MermaidSpec
import modular.mermaid.tasks.WriteMarkdownLegend
import modular.mermaid.tasks.WriteMermaidChart
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

internal fun Project.registerMermaidTrunkTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpecImpl,
) {
  WriteMarkdownLegend.register(
    target = this,
    name = WriteMarkdownLegend.TASK_NAME,
    extension = extension,
    outputFile = defaultOutputFile(extension, spec).map { f ->
      // just to change the extension! right faff
      val file = f.asFile
      val siblingFile = file.resolveSibling("${file.nameWithoutExtension}.md")
      layout.projectDirectory.file(siblingFile.relativeTo(projectDir).path)
    },
  )

  // Also validate the legend's dotfile when we call gradle check
  val tempTask = WriteMarkdownLegend.register(
    target = this,
    name = WriteMarkdownLegend.TASK_NAME_FOR_CHECKING,
    extension = extension,
    outputFile = modularBuildDirectory.map { it.file("legend-temp.md") },
  )

  val checkTask = CheckFileDiff.register(
    target = this,
    name = CheckFileDiff.legendName(flavor = "Mermaid"),
    generateTask = tempTask,
    realFile = outputFile(extension.outputs, Variant.Legend, fileExtension = "md"),
  )

  tasks.maybeCreate("check").dependsOn(checkTask)
}

internal fun Project.registerMermaidLeafTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpec,
  file: RegularFile,
) {
  val chartTask = WriteMermaidChart.register(
    target = this,
    name = WriteMermaidChart.TASK_NAME,
    extension = extension,
    spec = spec,
    outputFile = file,
  )

  val tempMermaidTask = WriteMermaidChart.register(
    target = this,
    name = WriteMermaidChart.TASK_NAME_FOR_CHECKING,
    extension = extension,
    spec = spec,
    outputFile = modularBuildDirectory.get().file("modules-temp.mmd"),
  )

  val checkTask = CheckFileDiff.register(
    target = this,
    name = CheckFileDiff.chartName(flavor = "Mermaid"),
    generateTask = tempMermaidTask,
    realFile = file,
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  WriteReadme.register(
    target = this,
    enabled = spec.writeReadme,
    flavor = "Mermaid",
    chartFile = chartTask.map { it.outputFile.get() },
    legendTask = WriteMarkdownLegend.get(rootProject),
  )
}
