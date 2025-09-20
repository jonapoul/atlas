/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.core.tasks.defaultOutputFile
import modular.graphviz.tasks.RunGraphviz
import modular.graphviz.tasks.WriteGraphvizChart
import modular.graphviz.tasks.WriteGraphvizLegend
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

internal fun Project.registerGraphVizTrunkTasks(
  extension: ModularExtensionImpl,
  spec: GraphVizSpecImpl,
) {
  val legendTask = WriteGraphvizLegend.register(
    target = this,
    name = "writeGraphvizLegend",
    extension = extension,
    outputFile = defaultOutputFile(extension, spec),
  )

  RunGraphviz.register(
    target = this,
    name = "generateGraphvizLegend",
    extension = extension,
    spec = spec,
    variant = Variant.Legend,
    dotFileTask = legendTask,
  )

  // Also validate the legend's dotfile when we call gradle check
  val tempTask = WriteGraphvizLegend.register(
    target = this,
    name = "writeGraphvizLegendForChecking",
    extension = extension,
    outputFile = modularBuildDirectory.map { it.file("legend-temp.dot") },
  )

  val checkTask = CheckFileDiff.register(
    target = this,
    name = CheckFileDiff.legendName(flavor = "Graphviz"),
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
  val dotFileTask = WriteGraphvizChart.register(
    target = this,
    name = WriteGraphvizChart.TASK_NAME,
    extension = extension,
    spec = spec,
    outputFile = file,
    printOutput = true,
  )

  val tempDotFileTask = WriteGraphvizChart.register(
    target = this,
    name = WriteGraphvizChart.TASK_NAME_FOR_CHECKING,
    extension = extension,
    spec = spec,
    outputFile = modularBuildDirectory.get().file("modules-temp.dot"),
    printOutput = false,
  )

  val checkTask = CheckFileDiff.register(
    target = this,
    name = CheckFileDiff.chartName(flavor = "Graphviz"),
    generateTask = tempDotFileTask,
    realFile = file,
  )

  tasks.maybeCreate("check").dependsOn(checkTask)

  val graphvizTask = RunGraphviz.register(
    target = this,
    name = "generateGraphvizChart",
    extension = extension,
    spec = spec,
    variant = Variant.Chart,
    dotFileTask = dotFileTask,
  )

  WriteReadme.register(
    target = this,
    enabled = spec.writeReadme,
    flavor = "Graphviz",
    chartFile = graphvizTask.map { it.outputFile.get() },
    legendTask = rootProject.tasks.named("generateGraphvizLegend", RunGraphviz::class.java),
  )
}
