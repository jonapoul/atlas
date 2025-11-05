package atlas.d2.tasks

import atlas.core.InternalAtlasApi
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.Variant
import atlas.core.internal.logIfConfigured
import atlas.core.internal.withExtension
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import atlas.d2.D2Spec
import atlas.d2.FileFormat
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
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

/**
 * Executes `d2` with the configured inputs to generate an image file. Requires D2 to be pre-installed.
 *
 * Just so I don't forget, [classesFile] is only used to force regeneration if the classes file updates, since we don't
 * directly read it in this task.
 */
@CacheableTask
public abstract class ExecD2 : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFile] public abstract val classesFile: RegularFileProperty
  @get:[PathSensitive(NONE) InputFile] public abstract val inputFile: RegularFileProperty
  @get:Input public abstract val outputFormat: Property<FileFormat>
  @get:[Input Optional] public abstract val animateInterval: Property<Int>
  @get:[Input Optional] public abstract val cliArguments: MapProperty<String, String>
  @get:[Input Optional] public abstract val pathToD2Command: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty
  @get:Inject public abstract val execOperations: ExecOperations

  init {
    group = ATLAS_TASK_GROUP
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription(): String = "Uses D2 to convert a text diagram into a ${outputFormat.get()} file"

  @TaskAction
  public fun execute() {
    val inputFile = inputFile.get().asFile.absolutePath
    val outputFile = outputFile.get().asFile
    val d2Executable = pathToD2Command.getOrElse("d2")
    val cliArguments = cliArguments.getOrElse(mutableMapOf())

    if (outputFormat.get() == FileFormat.Gif) {
      cliArguments += "animate-interval" to animateInterval.get().toString()
    }

    val errorBuffer = ByteArrayOutputStream()
    val command = buildList {
      add(d2Executable)
      add(inputFile)
      add(outputFile)
      cliArguments.forEach { (key, value) -> add("--$key=$value") }
    }

    logger.info("Starting d2: '$command'")
    val result = execOperations.exec { spec ->
      spec.errorOutput = errorBuffer
      spec.standardOutput = outputFile.outputStream()
      spec.isIgnoreExitValue = true
      spec.commandLine(command)
    }

    if (result.exitValue != 0) {
      val cmd = command.joinToString(separator = " ")
      throw GradleException("Error code ${result.exitValue} running '$cmd':\n $errorBuffer")
    }

    logIfConfigured(outputFile)
  }

  @InternalAtlasApi
  internal companion object {
    @InternalAtlasApi
    internal fun get(target: Project, name: String): TaskProvider<ExecD2> =
      target.tasks.named(name, ExecD2::class.java)

    @InternalAtlasApi
    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      spec: D2Spec,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): TaskProvider<ExecD2> = with(target) {
      val name = "execD2$variant"
      val execGraphviz = tasks.register(name, ExecD2::class.java)
      val d2Classes = WriteD2Classes.get(rootProject)

      execGraphviz.configure { task ->
        val d2File = dotFileTask.flatMap { it.outputFile }
        val outputFile = d2File.withExtension(target, extension = provider { spec.fileFormat.get() })

        task.classesFile.convention(d2Classes.flatMap { it.outputFile })
        task.inputFile.convention(d2File)
        task.pathToD2Command.convention(spec.pathToD2Command)
        task.outputFormat.convention(spec.fileFormat)
        task.outputFile.convention(outputFile)
        task.cliArguments.convention(spec.layoutEngine.properties)
        task.animateInterval.convention(spec.animateInterval)
      }

      return execGraphviz
    }
  }
}
