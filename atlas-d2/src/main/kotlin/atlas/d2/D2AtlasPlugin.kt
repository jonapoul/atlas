/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package atlas.d2

import atlas.core.AtlasPlugin
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.outputFile
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.WriteReadme
import atlas.d2.internal.D2AtlasExtensionImpl
import atlas.d2.tasks.ExecD2
import atlas.d2.tasks.WriteD2Chart
import atlas.d2.tasks.WriteD2Classes
import org.gradle.api.Project

public class D2AtlasPlugin : AtlasPlugin() {
  private lateinit var d2Extension: D2AtlasExtensionImpl
  override val extension: AtlasExtensionImpl by lazy { d2Extension }

  override fun applyToRoot(target: Project): Unit = with(target) {
    d2Extension = extensions.create(
      D2AtlasExtension::class.java,
      AtlasExtensionImpl.NAME,
      D2AtlasExtensionImpl::class.java,
    ) as D2AtlasExtensionImpl

    super.applyToRoot(target)

    afterEvaluate {
      warnIfFileFormatRequiresPlaywright()
      warnIfLabelLocationSpecifiedButNotPosition()
      warnIfAnimationSelectedWithNonAnimatedFileFormat()
    }
  }

  override fun applyToChild(target: Project) {
    d2Extension = target.rootProject
      .extensions
      .getByType(D2AtlasExtension::class.java) as D2AtlasExtensionImpl

    super.applyToChild(target)
  }

  override fun Project.registerChildTasks() {
    val d2Spec = d2Extension.d2

    // need to use the same pathToClassesFile string for real and dummy tasks, otherwise the check operation might
    // fail if the module and the build directory have different relative paths.
    val writeD2Classes = WriteD2Classes.get(rootProject)
    val classesFile = writeD2Classes.map { it.outputFile.get() }
    val outputFile = outputFile(Chart, d2Spec.fileExtension.get())
    val pathToClassesFile = classesFile.map { it.asFile.relativeTo(outputFile.parentFile).path }

    val chartTask = WriteD2Chart.real(
      target = project,
      extension = extension,
      outputFile = outputFile,
      pathToClassesFile = pathToClassesFile,
    )

    val dummyChartTask = WriteD2Chart.dummy(
      target = project,
      extension = extension,
      outputFile = atlasBuildDirectory.get().file("chart-temp.d2").asFile,
      pathToClassesFile = pathToClassesFile,
    )

    CheckFileDiff.register(
      target = project,
      extension = extension,
      spec = d2Spec,
      variant = Chart,
      realTask = chartTask,
      dummyTask = dummyChartTask,
    )

    val d2Task = ExecD2.register(
      target = project,
      spec = d2Spec,
      variant = Chart,
      dotFileTask = chartTask,
    )

    WriteReadme.register(
      target = project,
      flavor = "D2",
      chartFile = d2Task.map { it.outputFile.get() },
      legendTask = null,
    )
  }

  override fun Project.registerRootTasks() {
    val d2 = d2Extension.d2

    val classes = WriteD2Classes.real(
      target = this,
      extension = d2Extension,
      outputFile = outputFile(variant = Legend, fileExtension = "d2", filename = "classes"),
    )

    val dummyClasses = WriteD2Classes.dummy(
      target = project,
      extension = d2Extension,
      outputFile = atlasBuildDirectory.get().file("classes-temp.d2").asFile,
    )

    CheckFileDiff.register(
      target = project,
      extension = extension,
      spec = d2,
      variant = Chart,
      realTask = classes,
      dummyTask = dummyClasses,
    )
  }

  private fun Project.warnIfFileFormatRequiresPlaywright() {
    val d2 = d2Extension.d2
    val format = d2.fileFormat.get()
    val shouldSuppress = d2.properties.suppressPlaywrightWarning.get()
    val simpleFormats = setOf(FileFormat.Svg, FileFormat.Ascii)
    if (format !in simpleFormats && !shouldSuppress) {
      logger.warn(
        "Warning: Most of D2's output formats (including your selection: $format) require installation of " +
          "Playwright for image conversion. Depending on your OS, this might need to download a build of Chromium " +
          "to run Playwright. See https://github.com/terrastruct/d2/issues/2502 for a bit more context. " +
          "If you want to suppress this warning, add 'atlas.d2.suppressPlaywrightWarning=true' to your " +
          "gradle.properties file.",
      )
    }
  }

  private fun Project.warnIfLabelLocationSpecifiedButNotPosition() {
    val d2 = d2Extension.d2
    val position = d2.groupLabelPosition.orNull
    val location = d2.groupLabelLocation.orNull
    val shouldSuppress = d2.properties.suppressLabelLocationWarning.get()
    if (position == null && location != null && !shouldSuppress) {
      logger.warn(
        "Warning: you've configured groupLabelLocation but not groupLabelPosition - this is not supported in D2 " +
          "diagrams. If you want to suppress this warning, add 'atlas.d2.suppressLabelLocationWarning=true' to " +
          "your gradle.properties file.",
      )
    }
  }

  private fun Project.warnIfAnimationSelectedWithNonAnimatedFileFormat() {
    val d2 = d2Extension.d2
    val format = d2.fileFormat.get()
    val animatedFormats = setOf(FileFormat.Svg, FileFormat.Gif)
    val animated = d2.animateLinks.orNull
    val shouldSuppress = d2.properties.suppressAnimationWarning.get()
    if (animated == true && format !in animatedFormats && !shouldSuppress) {
      logger.warn(
        "Warning: you've configured animateLinks but chosen a non-animatable file format ($format). If you want to " +
          "suppress this warning, add 'atlas.d2.suppressAnimationWarning=true' to your gradle.properties file.",
      )
    }
  }
}
