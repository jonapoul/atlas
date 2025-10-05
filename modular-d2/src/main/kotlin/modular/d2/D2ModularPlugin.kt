/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.d2

import modular.core.ModularPlugin
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.tasks.CheckFileDiff
import modular.d2.internal.D2ModularExtensionImpl
import modular.d2.tasks.ExecD2
import modular.d2.tasks.WriteD2Chart
import modular.d2.tasks.WriteD2ChartBase
import modular.d2.tasks.WriteDummyD2Chart
import org.gradle.api.Project

class D2ModularPlugin : ModularPlugin<D2ModularExtensionImpl>() {
  override fun Project.createExtension() = extensions.create(
    D2ModularExtension::class.java,
    ModularExtensionImpl.NAME,
    D2ModularExtensionImpl::class.java,
  ) as D2ModularExtensionImpl

  override fun Project.getExtension() =
    rootProject.extensions.getByType(D2ModularExtension::class.java) as D2ModularExtensionImpl

  override fun applyToRoot(target: Project) = with(target) {
    super.applyToRoot(target)

    afterEvaluate {
      warnIfFileFormatRequiresPlaywright()
      warnIfLabelLocationSpecifiedButNotPosition()
    }
  }

  override fun Project.registerChildTasks() {
    val d2Spec = extension.d2

    val chartTask = WriteD2ChartBase.register<WriteD2Chart>(
      target = project,
      extension = extension,
      spec = d2Spec,
      outputFile = outputFile(Chart, d2Spec.fileExtension.get()),
    )

    val dummyChartTask = WriteD2ChartBase.register<WriteDummyD2Chart>(
      target = project,
      extension = extension,
      spec = d2Spec,
      outputFile = modularBuildDirectory.get().file("chart-temp.d2").asFile,
    )

    CheckFileDiff.register(
      target = project,
      extension = extension,
      spec = d2Spec,
      variant = Chart,
      realTask = chartTask,
      dummyTask = dummyChartTask,
    )

    ExecD2.register(
      target = project,
      spec = d2Spec,
      variant = Chart,
      dotFileTask = chartTask,
    )

    //    WriteReadme.register(
    //      target = project,
    //      flavor = "D2",
    //      chartFile = d2Task.map { it.outputFile.get() },
    //      legendTask = rootProject.tasks.named("execD2Legend", ExecD2::class.java),
    //    )
  }

  override fun Project.registerRootTasks() {
    //    val spec = extension.d2
    //
    //    val realTask = WriteD2LegendBase.register<WriteD2Legend>(
    //      target = project,
    //      spec = spec,
    //      extension = extension,
    //      outputFile = outputFile(Legend, spec.fileExtension.get()),
    //    )
    //
    //    ExecD2.register(
    //      target = project,
    //      spec = spec,
    //      variant = Legend,
    //      dotFileTask = realTask,
    //    )
    //
    //    // Also validate the legend's dotfile when we call gradle check
    //    val dummyTask = WriteD2LegendBase.register<WriteDummyD2Legend>(
    //      target = project,
    //      spec = spec,
    //      extension = extension,
    //      outputFile = modularBuildDirectory.get().file("legend-temp.dot").asFile,
    //    )
    //
    //    CheckFileDiff.register(
    //      target = project,
    //      extension = extension,
    //      spec = spec,
    //      variant = Legend,
    //      realTask = realTask,
    //      dummyTask = dummyTask,
    //    )
  }

  private fun Project.warnIfFileFormatRequiresPlaywright() {
    val d2 = extension.d2
    val format = d2.fileFormat.get()
    val shouldSuppress = d2.properties.suppressPlaywrightWarning.get()
    val simpleFormats = setOf(FileFormat.Svg, FileFormat.Ascii)
    if (format !in simpleFormats && !shouldSuppress) {
      logger.warn(
        "Warning: Most of D2's output formats (including your selection: $format) require installation of " +
          "Playwright for image conversion. Depending on your OS, this might need to download a build of Chromium " +
          "to run Playwright. See https://github.com/terrastruct/d2/issues/2502 for a bit more context. " +
          "If you want to suppress this warning, add 'modular.d2.suppressPlaywrightWarning=true' to your " +
          "gradle.properties file.",
      )
    }
  }

  private fun Project.warnIfLabelLocationSpecifiedButNotPosition() {
    val d2 = extension.d2
    val position = d2.groupLabelPosition.orNull
    val location = d2.groupLabelLocation.orNull
    val shouldSuppress = d2.properties.suppressLabelLocationWarning.get()
    if (position == null && location != null && !shouldSuppress) {
      logger.warn(
        "Warning: you've configured groupLabelLocation but not groupLabelPosition - this is not supported in D2 " +
          "diagrams. If you want to suppress this warning, add 'modular.d2.suppressLabelLocationWarning=true' to " +
          "your gradle.properties file.",
      )
    }
  }
}
