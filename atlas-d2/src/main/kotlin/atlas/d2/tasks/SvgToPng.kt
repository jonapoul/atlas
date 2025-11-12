package atlas.d2.tasks

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.logIfConfigured
import atlas.core.internal.withExtension
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@CacheableTask
public abstract class SvgToPng : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFile] public abstract val inputFile: RegularFileProperty
  @get:[Input Optional] public abstract val converter: Property<Converter>
  @get:OutputFile abstract override val outputFile: RegularFileProperty
  @get:Inject public abstract val execOperations: ExecOperations

  init {
    group = ATLAS_TASK_GROUP
    description = "Converts an SVG file to PNG format"
  }

  @TaskAction
  @Suppress("ThrowsCount")
  public fun execute() {
    val inputFile = inputFile.get().asFile
    if (!inputFile.exists()) {
      throw GradleException("Input SVG file does not exist: ${inputFile.absolutePath}")
    }

    val outputFile = outputFile.get().asFile
    val converter = converter.get()
    val command = buildConverterCommand(converter, inputFile.absolutePath, outputFile.absolutePath)
    logger.info("Converting SVG to PNG using '$converter': $command")

    val errorBuffer = ByteArrayOutputStream()
    val buffer = ByteArrayOutputStream()
    val result = execOperations.exec { spec ->
      spec.errorOutput = errorBuffer
      spec.standardOutput = buffer
      spec.isIgnoreExitValue = true
      spec.commandLine(command)
    }

    logger.info("Output buffer = '$buffer'")

    if (result.exitValue != 0) {
      val cmd = command.joinToString(separator = " ")
      throw GradleException("Error code ${result.exitValue} converting SVG to PNG with '$cmd':\n$errorBuffer")
    }

    if (!outputFile.exists()) {
      throw GradleException("Output PNG file was not created: ${outputFile.absolutePath}")
    }

    logIfConfigured(outputFile)
  }

  private fun buildConverterCommand(
    converter: Converter,
    inputPath: String,
    outputPath: String,
  ): List<String> = when (converter) {
    Converter.ImageMagick6 -> listOf(converter.value, inputPath, outputPath)
    Converter.ImageMagick7 -> listOf(converter.value, inputPath, outputPath)
    Converter.Inkscape -> listOf(converter.value, inputPath, "--export-type=png", "--export-filename=$outputPath")
    Converter.LibRsvg -> listOf(converter.value, "-o", outputPath, inputPath)
    Converter.CairoSvg -> listOf(converter.value, inputPath, "-o", outputPath)
  }

  public enum class Converter(internal val value: String) {
    ImageMagick7("magick"),
    ImageMagick6("convert"),
    Inkscape("inkscape"),
    LibRsvg("rsvg-convert"),
    CairoSvg("cairosvg"),
    ;

    override fun toString(): String = value
  }

  internal companion object {
    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      svgTask: TaskProvider<T>,
      isEnabled: Provider<Boolean>,
      converter: Property<Converter>,
    ): TaskProvider<SvgToPng> = with(target) {
      tasks.register("svgToPng", SvgToPng::class.java) { task ->
        task.converter.convention(converter)
        task.inputFile.convention(svgTask.flatMap { it.outputFile })
        task.outputFile.convention(
          svgTask.flatMap { t ->
            t.outputFile.withExtension(target, provider { "png" })
          },
        )
        task.onlyIf { isEnabled.get() }
      }
    }
  }
}
