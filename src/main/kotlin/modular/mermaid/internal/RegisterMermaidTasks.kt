/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.modularBuildDirectory
import modular.internal.outputFile
import modular.mermaid.spec.MermaidSpec
import modular.mermaid.tasks.GenerateLegendMarkdownTask
import modular.mermaid.tasks.GenerateModulesMermaidTask
import modular.tasks.CheckFileDiffTask
import modular.tasks.defaultOutputFile
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.TaskProvider

internal fun Project.registerMermaidTrunkTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpecImpl,
  generateLegend: TaskProvider<Task>,
) {
  val legendTask = GenerateLegendMarkdownTask.register(
    target = this,
    name = GenerateLegendMarkdownTask.TASK_NAME,
    extension = extension,
    outputFile = defaultOutputFile(extension, spec).map { f ->
      // just to change the extension! right faff
      val file = f.asFile
      val siblingFile = file.resolveSibling("${file.nameWithoutExtension}.md")
      layout.projectDirectory.file(siblingFile.relativeTo(projectDir).path)
    },
  )

  // Also validate the legend's dotfile when we call gradle check
  val tempTask = GenerateLegendMarkdownTask.register(
    target = this,
    name = GenerateLegendMarkdownTask.TASK_NAME_FOR_CHECKING,
    extension = extension,
    outputFile = modularBuildDirectory.map { it.file("legend-temp.md") },
  )

  val checkTask = CheckFileDiffTask.register(
    target = this,
    name = CheckFileDiffTask.NAME_LEGEND_BASE + "Mermaid",
    generateTask = tempTask,
    realFile = outputFile(extension.outputs, Variant.Legend, fileExtension = "md"),
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  generateLegend.configure { t ->
    t.dependsOn(legendTask)
  }
}

internal fun Project.registerMermaidLeafTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpec,
  file: RegularFile,
  generateModules: TaskProvider<Task>,
) {
  val mermaidTask = GenerateModulesMermaidTask.register(
    target = this,
    name = GenerateModulesMermaidTask.TASK_NAME,
    extension = extension,
    spec = spec,
    outputFile = file,
    printOutput = true,
  )

  val tempMermaidTask = GenerateModulesMermaidTask.register(
    target = this,
    name = GenerateModulesMermaidTask.TASK_NAME_FOR_CHECKING,
    extension = extension,
    spec = spec,
    outputFile = modularBuildDirectory.get().file("modules-temp.mmd"),
    printOutput = false,
  )

  val checkTask = CheckFileDiffTask.register(
    target = this,
    name = CheckFileDiffTask.NAME_MODULES_BASE + "Mermaid",
    generateTask = tempMermaidTask,
    realFile = file,
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  generateModules.configure { t ->
    t.dependsOn(mermaidTask)
  }
}
