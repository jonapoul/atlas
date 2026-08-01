package atlas.d2.internal

import atlas.core.Framework
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.ChartFiles
import atlas.core.internal.FrameworkTasks
import atlas.core.internal.Variant.Chart
import atlas.core.internal.Variant.Legend
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.outputFile
import atlas.core.tasks.CheckFileDiff
import atlas.d2.FileFormat
import atlas.d2.tasks.ExecD2
import atlas.d2.tasks.SvgToPng
import atlas.d2.tasks.WriteD2Chart
import atlas.d2.tasks.WriteD2Classes
import org.gradle.api.Project

internal object D2Tasks : FrameworkTasks {
  override val framework: Framework = Framework.D2

  override fun registerRootTasks(target: Project, extension: AtlasExtensionImpl): Unit =
    with(target) {
      val d2 = extension.d2

      warnIfFileFormatRequiresPlaywright(extension)
      warnIfLabelLocationSpecifiedButNotPosition(extension)
      warnIfAnimationSelectedWithNonAnimatedFileFormat(extension)

      val classes =
        WriteD2Classes.real(
          target = this,
          extension = extension,
          outputFile =
            outputFile(
              framework = framework,
              variant = Legend,
              fileExtension = "d2",
              filename = "classes",
            ),
        )

      val dummyClasses =
        WriteD2Classes.dummy(
          target = project,
          extension = extension,
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

  override fun registerChildTasks(target: Project, extension: AtlasExtensionImpl): ChartFiles =
    with(target) {
      val d2Spec = extension.d2

      // need to use the same pathToClassesFile string for real and dummy tasks, otherwise the check
      // operation might fail if the project and the build directory have different relative paths.
      val writeD2Classes = WriteD2Classes.get(rootProject)
      val classesFile = writeD2Classes.flatMap { it.outputFile }
      val outputFile = outputFile(framework, Chart, d2Spec.fileExtension.get())
      val pathToClassesFile = classesFile.map { it.asFile.relativeTo(outputFile.parentFile).path }

      val chartTask =
        WriteD2Chart.real(
          target = project,
          extension = extension,
          outputFile = outputFile,
          pathToClassesFile = pathToClassesFile,
        )

      val dummyChartTask =
        WriteD2Chart.dummy(
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

      val d2Task =
        ExecD2.register(
          target = project,
          spec = d2Spec,
          variant = Chart,
          dotFileTask = chartTask,
        )

      val isSvgInput = d2Spec.fileFormat.map { it == FileFormat.Svg }
      val runSvgToPng = provider { isSvgInput.get() && d2Spec.converter.isPresent }

      val svgToPng =
        SvgToPng.register(
          target = project,
          svgTask = d2Task,
          isEnabled = runSvgToPng,
          converter = d2Spec.converter,
        )

      val taskForReadme = svgToPng.flatMap { if (runSvgToPng.get()) svgToPng else d2Task }

      ChartFiles(
        framework = framework,
        chart = taskForReadme.flatMap { it.outputFile },
        legend = null,
      )
    }

  private fun Project.warnIfFileFormatRequiresPlaywright(extension: AtlasExtensionImpl) {
    val d2 = extension.d2
    val format = d2.fileFormat.get()
    val shouldSuppress = d2.properties.suppressPlaywrightWarning.get()
    val simpleFormats = setOf(FileFormat.Svg, FileFormat.Ascii)
    if (format !in simpleFormats && !shouldSuppress) {
      logger.warn(
        "Warning: Most of D2's output formats (including your selection: $format) require installation of " +
          "Playwright for image conversion. Depending on your OS, this might need to download a build of Chromium " +
          "to run Playwright. See https://github.com/terrastruct/d2/issues/2502 for a bit more context. " +
          "If you want to suppress this warning, add 'atlas.d2.suppressPlaywrightWarning=true' to your " +
          "gradle.properties file."
      )
    }
  }

  private fun Project.warnIfLabelLocationSpecifiedButNotPosition(extension: AtlasExtensionImpl) {
    val d2 = extension.d2
    val position = d2.groupLabelPosition.orNull
    val location = d2.groupLabelLocation.orNull
    val shouldSuppress = d2.properties.suppressLabelLocationWarning.get()
    if (position == null && location != null && !shouldSuppress) {
      logger.warn(
        "Warning: you've configured groupLabelLocation but not groupLabelPosition - this is not supported in D2 " +
          "diagrams. If you want to suppress this warning, add 'atlas.d2.suppressLabelLocationWarning=true' to " +
          "your gradle.properties file."
      )
    }
  }

  private fun Project.warnIfAnimationSelectedWithNonAnimatedFileFormat(
    extension: AtlasExtensionImpl
  ) {
    val d2 = extension.d2
    val format = d2.fileFormat.get()
    val animatedFormats = setOf(FileFormat.Svg, FileFormat.Gif)
    val animated = d2.animateLinks.orNull
    val shouldSuppress = d2.properties.suppressAnimationWarning.get()
    if (animated == true && format !in animatedFormats && !shouldSuppress) {
      logger.warn(
        "Warning: you've configured animateLinks but chosen a non-animatable file format ($format). If you want to " +
          "suppress this warning, add 'atlas.d2.suppressAnimationWarning=true' to your gradle.properties file."
      )
    }
  }
}
