package atlas.d2.tasks

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.logIfConfigured
import atlas.core.internal.withExtension
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import java.io.ByteArrayOutputStream
import javax.inject.Inject
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
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.process.ExecOperations

@CacheableTask
public abstract class SvgToPng : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFile]
  public abstract val inputFile: RegularFileProperty
  @get:Input public abstract val converter: Property<Converter>
  @get:[Input Optional]
  public abstract val scale: Property<Float>
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
    val command =
      buildConverterCommand(
        converter,
        inputFile.absolutePath,
        outputFile.absolutePath,
        scale.orNull,
      )
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
      throw GradleException(
        "Error code ${result.exitValue} converting SVG to PNG with '$cmd':\n$errorBuffer"
      )
    }

    if (!outputFile.exists()) {
      throw GradleException("Output PNG file was not created: ${outputFile.absolutePath}")
    }

    logIfConfigured(outputFile)
  }

  @Suppress("CyclomaticComplexMethod")
  private fun buildConverterCommand(
    converter: Converter,
    inputPath: String,
    outputPath: String,
    scale: Float?,
  ): List<String> =
    when (converter) {
      ImageMagick6,
      ImageMagick7 ->
        buildList {
          add(converter.value)
          // -density must precede the input file to affect rasterization, not just the output size
          if (scale != null) {
            add("-density")
            add((DEFAULT_DPI * scale).toString())
          }
          add(inputPath)
          add(outputPath)
        }

      Inkscape ->
        buildList {
          add(converter.value)
          add(inputPath)
          add("--export-type=png")
          add("--export-filename=$outputPath")
          if (scale != null) {
            add("--export-dpi=${DEFAULT_DPI * scale}")
          }
        }

      LibRsvg ->
        buildList {
          add(converter.value)
          add("-o")
          add(outputPath)
          if (scale != null) {
            add("-z")
            add(scale.toString())
          }
          add(inputPath)
        }

      CairoSvg ->
        buildList {
          add(converter.value)
          add(inputPath)
          add("-o")
          add(outputPath)
          if (scale != null) {
            add("--scale")
            add(scale.toString())
          }
        }
    }

  public enum class Converter(internal val value: String) {
    ImageMagick7("magick"),
    ImageMagick6("convert"),
    Inkscape("inkscape"),
    LibRsvg("rsvg-convert"),
    CairoSvg("cairosvg");

    override fun toString(): String = value
  }

  internal companion object {
    /** Assumed DPI baseline that each converter's density/DPI/zoom flag scales relative to. */
    private const val DEFAULT_DPI = 96f

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      svgTask: TaskProvider<T>,
      isEnabled: Provider<Boolean>,
      converter: Property<Converter>,
      scale: Property<Float>,
    ): TaskProvider<SvgToPng> =
      with(target) {
        tasks.register("svgToPng", SvgToPng::class.java) { task ->
          task.converter.convention(converter)
          task.scale.convention(scale)
          task.inputFile.convention(svgTask.flatMap { it.outputFile })
          task.outputFile.convention(
            svgTask.flatMap { t ->
              t.outputFile.withExtension(target, provider { "png" })
            }
          )
          task.onlyIf { isEnabled.get() }
        }
      }
  }
}
