package atlas.d2.tasks

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasContext
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.logIfConfigured
import atlas.core.internal.qualifier
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import atlas.d2.internal.D2ClassesConfig
import atlas.d2.internal.toConfig
import atlas.d2.internal.writeD2Classes
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.work.DisableCachingByDefault

@CacheableTask
public abstract class WriteD2Classes : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  @get:Input public abstract val config: Property<D2ClassesConfig>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates a D2 global classes file"
  }

  @TaskAction
  public open fun execute() {
    val outputFile = outputFile.get().asFile
    val contents = writeD2Classes(config.get())
    outputFile.writeText(contents)
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteD2ClassesDummy : WriteD2Classes(), DummyAtlasGenerationTask

  internal companion object {
    private const val NAME = "writeD2Classes"

    internal fun get(target: Project): TaskProvider<WriteD2Classes> =
      target.tasks.named(NAME, WriteD2Classes::class.java)

    internal fun real(context: AtlasContext, outputFile: File) =
      register<WriteD2Classes>(context, outputFile)

    internal fun dummy(context: AtlasContext, outputFile: File) =
      register<WriteD2ClassesDummy>(context, outputFile)

    private inline fun <reified T : WriteD2Classes> register(
      context: AtlasContext,
      outputFile: File,
    ): TaskProvider<T> =
      with(context.project) {
        val name = "write${T::class.qualifier}D2Classes"
        val writeClasses =
          tasks.register(name, T::class.java) { task ->
            task.outputFile.set(outputFile)
          }

        writeClasses.configure { task ->
          task.config.convention(context.toConfig())
        }

        writeClasses
      }
  }
}
