/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.WriteReadme
import modular.graphviz.tasks.ExecGraphviz
import modular.graphviz.tasks.WriteDummyGraphvizChart
import modular.graphviz.tasks.WriteDummyGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizChart
import modular.graphviz.tasks.WriteGraphvizChartBase
import modular.graphviz.tasks.WriteGraphvizLegend
import modular.graphviz.tasks.WriteGraphvizLegendBase
import org.gradle.api.Project

internal fun Project.registerGraphvizTrunkTasks(
  extension: ModularExtensionImpl,
  spec: GraphvizSpecImpl,
) {
  val realTask = WriteGraphvizLegendBase.register<WriteGraphvizLegend>(
    target = project,
    variant = Legend,
    spec = spec,
    extension = extension,
    outputFile = outputFile(extension, Legend, spec.fileExtension.get()),
  )

  val graphvizTask = ExecGraphviz.register(
    target = project,
    spec = spec,
    variant = Legend,
    dotFileTask = realTask,
  )

  // Also validate the legend's dotfile when we call gradle check
  val dummyTask = WriteGraphvizLegendBase.register<WriteDummyGraphvizLegend>(
    target = project,
    variant = Legend,
    spec = spec,
    extension = extension,
    outputFile = modularBuildDirectory.get().file("legend-temp.dot").asFile,
  )

  CheckFileDiff.register(
    target = project,
    extension = extension,
    spec = spec,
    variant = Legend,
    realTask = realTask,
    dummyTask = dummyTask,
  )

  WriteReadme.register(
    target = project,
    enabled = spec.writeReadme,
    flavor = "Graphviz",
    chartFile = graphvizTask.map { it.outputFile.get() },
    legendTask = rootProject.tasks.named("execGraphvizLegend", ExecGraphviz::class.java),
  )
}

internal fun Project.registerGraphvizLeafTasks(
  extension: ModularExtensionImpl,
  spec: GraphvizSpecImpl,
) {
  val dotTask = WriteGraphvizChartBase.register<WriteGraphvizChart>(
    target = project,
    extension = extension,
    spec = spec,
    variant = Chart,
    outputFile = outputFile(extension, Chart, spec.fileExtension.get()),
    printOutput = true,
  )

  val dummyDotTask = WriteGraphvizChartBase.register<WriteDummyGraphvizChart>(
    target = project,
    extension = extension,
    spec = spec,
    variant = Chart,
    outputFile = modularBuildDirectory.get().file("modules-temp.dot").asFile,
    printOutput = false,
  )

  CheckFileDiff.register(
    target = project,
    extension = extension,
    spec = spec,
    variant = Chart,
    realTask = dotTask,
    dummyTask = dummyDotTask,
  )

  ExecGraphviz.register(
    target = project,
    spec = spec,
    variant = Chart,
    dotFileTask = dotTask,
  )
}
