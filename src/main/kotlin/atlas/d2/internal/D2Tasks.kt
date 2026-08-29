package atlas.d2.internal

import atlas.core.Framework
import atlas.core.internal.AtlasContext
import atlas.core.internal.ChartFiles
import atlas.core.internal.FrameworkTasks
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.outputFile
import atlas.core.internal.publishAtlasArtifact
import atlas.core.internal.singleFile
import atlas.core.tasks.CheckFileDiff
import atlas.d2.FileFormat
import atlas.d2.tasks.ExecD2
import atlas.d2.tasks.SvgToPng
import atlas.d2.tasks.WriteD2Chart
import atlas.d2.tasks.WriteD2Classes
import org.gradle.api.Project

internal object D2Tasks : FrameworkTasks {
  override val framework: Framework = D2

  override fun registerRootTasks(context: AtlasContext): Unit =
    with(context.project) {
      val d2 = context.d2

      warnIfFileFormatRequiresChromium(context)
      warnIfLabelLocationSpecifiedButNotPosition(context)
      warnIfAnimationSelectedWithNonAnimatedFileFormat(context)

      val classes =
        WriteD2Classes.real(
          context = context,
          outputFile =
            outputFile(
              config = context.config,
              framework = framework,
              variant = Legend,
              fileExtension = "d2",
              filename = "classes",
            ),
        )

      // Every project's chart references this one file, and under isolated projects a subproject
      // can't reach the task that writes it, so publish it as an artifact instead.
      publishAtlasArtifact(
        artifact = D2Classes,
        file = classes.flatMap { it.outputFile },
        builtBy = classes,
      )

      val dummyClasses =
        WriteD2Classes.dummy(
          context = context,
          outputFile = atlasBuildDirectory.get().file("classes-temp.d2").asFile,
        )

      CheckFileDiff.register(
        target = this,
        config = context.config,
        spec = d2,
        variant = Chart,
        realTask = classes,
        dummyTask = dummyClasses,
      )
    }

  override fun registerChildTasks(context: AtlasContext): ChartFiles =
    with(context.project) {
      val d2Spec = context.d2

      // need to use the same pathToClassesFile string for real and dummy tasks, otherwise the check
      // operation might fail if the project and the build directory have different relative paths.
      val classesFile = context.fromRoot(D2Classes)
      val outputFile =
        outputFile(
          config = context.config,
          framework = framework,
          variant = Chart,
          fileExtension = d2Spec.fileExtension.get(),
        )
      val pathToClassesFile =
        classesFile.singleFile(D2Classes).map {
          it.relativeTo(outputFile.parentFile).path
        }

      val chartTask =
        WriteD2Chart.real(
          context = context,
          outputFile = outputFile,
          pathToClassesFile = pathToClassesFile,
        )

      val dummyChartTask =
        WriteD2Chart.dummy(
          context = context,
          outputFile = atlasBuildDirectory.get().file("chart-temp.d2").asFile,
          pathToClassesFile = pathToClassesFile,
        )

      CheckFileDiff.register(
        target = this,
        config = context.config,
        spec = d2Spec,
        variant = Chart,
        realTask = chartTask,
        dummyTask = dummyChartTask,
      )

      val d2Task =
        ExecD2.register(
          target = this,
          spec = d2Spec,
          variant = Chart,
          dotFileTask = chartTask,
          classesFile = classesFile,
        )

      val isSvgInput = d2Spec.fileFormat.map { it == Svg }
      val runSvgToPng = provider { isSvgInput.get() && d2Spec.converter.isPresent }

      val svgToPng =
        SvgToPng.register(
          target = this,
          svgTask = d2Task,
          isEnabled = runSvgToPng,
          converter = d2Spec.converter,
          scale = d2Spec.scale,
        )

      val taskForReadme = svgToPng.flatMap { if (runSvgToPng.get()) svgToPng else d2Task }

      ChartFiles(
        framework = framework,
        chart = taskForReadme.flatMap { it.outputFile },
        legend = null,
      )
    }

  private fun Project.warnIfFileFormatRequiresChromium(context: AtlasContext) {
    val d2 = context.d2
    val format = d2.fileFormat.get()
    val shouldSuppress = d2.properties.suppressPlaywrightWarning.get()
    val simpleFormats = setOf<FileFormat>(Svg, Ascii)
    if (format !in simpleFormats && !shouldSuppress) {
      logger.warn(
        "Warning: most of D2's output formats (including your selection: $format) are rendered through a bundled " +
          "Chromium, which D2 offers to download on first use. It asks for confirmation on stdin, so a Gradle build " +
          "has no way to answer and d2 fails with 'failed to read user input: EOF'. Either run d2 once by hand to " +
          "accept the download, or stick to SVG and use convertSvgToPng to rasterise it. See " +
          "https://github.com/d2lang/d2/issues/2502 for a bit more context. If you want to suppress this warning, " +
          "add 'atlas.d2.suppressPlaywrightWarning=true' to your gradle.properties file."
      )
    }
  }

  private fun Project.warnIfLabelLocationSpecifiedButNotPosition(context: AtlasContext) {
    val d2 = context.d2
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

  private fun Project.warnIfAnimationSelectedWithNonAnimatedFileFormat(context: AtlasContext) {
    val d2 = context.d2
    val format = d2.fileFormat.get()
    val animatedFormats = setOf<FileFormat>(Svg, Gif)
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
