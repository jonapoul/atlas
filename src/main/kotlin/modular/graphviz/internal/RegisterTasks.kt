/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.tasks.CheckDotFileTask
import modular.graphviz.tasks.GenerateGraphvizFileTask
import modular.graphviz.tasks.GenerateLegendDotFileTask
import modular.graphviz.tasks.GenerateModulesDotFileTask
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.modularBuildDirectory
import modular.internal.outputFile
import modular.tasks.MODULAR_TASK_GROUP
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.TaskProvider

internal fun Project.registerGraphVizTrunkTasks(
  extension: ModularExtensionImpl,
  spec: GraphVizSpec,
  generateLegend: TaskProvider<Task>,
) {
  val legend = spec.legend
  if (legend != null) {
    // Only create a legend if one of the legend functions was explicitly called
    val outputFile = GenerateLegendDotFileTask.defaultOutputFile(extension, spec)
    val dotFileTask = GenerateLegendDotFileTask.register(
      target = this,
      name = GenerateLegendDotFileTask.TASK_NAME,
      legendSpec = legend,
      extension = extension,
      outputFile = outputFile,
    )
    val graphVizTasks = GenerateGraphvizFileTask.register(this, extension, spec, Variant.Legend, dotFileTask)
    generateLegend.configure { it.dependsOn(graphVizTasks) }

    // Also validate the legend's dotfile when we call gradle check
    val tempTask = GenerateLegendDotFileTask.register(
      target = this,
      name = GenerateLegendDotFileTask.TASK_NAME_FOR_CHECKING,
      legendSpec = legend,
      extension = extension,
      outputFile = modularBuildDirectory.map { it.file("legend-temp.dot") },
    )

    val checkTask = CheckDotFileTask.register(
      target = this,
      name = CheckDotFileTask.NAME_LEGEND,
      generateDotFile = tempTask,
      realDotFile = outputFile(extension.outputs, Variant.Legend, fileExtension = spec.fileExtension.get()),
    )
    tasks.maybeCreate("check").dependsOn(checkTask)
  }
}

internal fun Project.registerGraphVizLeafTasks(
  extension: ModularExtensionImpl,
  spec: GraphVizSpec,
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

  val checkTask = CheckDotFileTask.register(
    target = this,
    name = CheckDotFileTask.NAME_MODULES,
    generateDotFile = tempDotFileTask,
    realDotFile = file,
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  val outputTasks = GenerateGraphvizFileTask.register(
    target = this,
    extension = extension,
    spec = spec,
    variant = Variant.Modules,
    dotFileTask = dotFileTask,
  )

  tasks.register("generateModules") { t ->
    t.group = MODULAR_TASK_GROUP
    t.description = "Wrapper task for the other 'generateModulesX' tasks"
    t.dependsOn(dotFileTask)
    t.dependsOn(outputTasks)
  }
}
