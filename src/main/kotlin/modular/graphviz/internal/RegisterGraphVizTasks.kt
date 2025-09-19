/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiffTask
import modular.core.tasks.defaultOutputFile
import modular.graphviz.tasks.GenerateGraphvizFileTask
import modular.graphviz.tasks.GenerateLegendDotFileTask
import modular.graphviz.tasks.GenerateModulesDotFileTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

internal fun Project.registerGraphVizTrunkTasks(
  extension: ModularExtensionImpl,
  spec: GraphVizSpecImpl,
) {
  val legendTask = GenerateLegendDotFileTask.register(
    target = this,
    name = GenerateLegendDotFileTask.TASK_NAME,
    extension = extension,
    outputFile = defaultOutputFile(extension, spec),
  )

  GenerateGraphvizFileTask.register(
    target = this,
    extension = extension,
    spec = spec,
    variant = Variant.Legend,
    dotFileTask = legendTask,
  )

  // Also validate the legend's dotfile when we call gradle check
  val tempTask = GenerateLegendDotFileTask.register(
    target = this,
    name = GenerateLegendDotFileTask.TASK_NAME_FOR_CHECKING,
    extension = extension,
    outputFile = modularBuildDirectory.map { it.file("legend-temp.dot") },
  )

  val checkTask = CheckFileDiffTask.register(
    target = this,
    name = CheckFileDiffTask.NAME_LEGEND_BASE + "DotFile",
    generateTask = tempTask,
    realFile = outputFile(extension.outputs, Variant.Legend, fileExtension = spec.fileExtension.get()),
  )

  tasks.maybeCreate("check").dependsOn(checkTask)
}

internal fun Project.registerGraphVizLeafTasks(
  extension: ModularExtensionImpl,
  spec: GraphVizSpecImpl,
  file: RegularFile,
) {
  val dotFileTask = GenerateModulesDotFileTask.register(
    target = this,
    name = GenerateModulesDotFileTask.TASK_NAME,
    extension = extension,
    spec = spec,
    outputFile = file,
    printOutput = true,
  )

  val tempDotFileTask = GenerateModulesDotFileTask.register(
    target = this,
    name = GenerateModulesDotFileTask.TASK_NAME_FOR_CHECKING,
    extension = extension,
    spec = spec,
    outputFile = modularBuildDirectory.get().file("modules-temp.dot"),
    printOutput = false,
  )

  val checkTask = CheckFileDiffTask.register(
    target = this,
    name = CheckFileDiffTask.NAME_MODULES_BASE + "DotFile",
    generateTask = tempDotFileTask,
    realFile = file,
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  GenerateGraphvizFileTask.register(
    target = this,
    extension = extension,
    spec = spec,
    variant = Variant.Chart,
    dotFileTask = dotFileTask,
  )
}
