/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2.tasks

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.logIfConfigured
import atlas.core.internal.qualifier
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import atlas.d2.internal.D2AtlasExtensionImpl
import atlas.d2.internal.D2ClassesConfig
import atlas.d2.internal.toConfig
import atlas.d2.internal.writeD2Classes
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
import java.io.File

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

    internal fun real(
      target: Project,
      extension: D2AtlasExtensionImpl,
      outputFile: File,
    ) = register<WriteD2Classes>(target, extension, outputFile)

    internal fun dummy(
      target: Project,
      extension: D2AtlasExtensionImpl,
      outputFile: File,
    ) = register<WriteD2ClassesDummy>(target, extension, outputFile)

    private inline fun <reified T : WriteD2Classes> register(
      target: Project,
      extension: D2AtlasExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val name = "write${T::class.qualifier}D2Classes"
      val writeClasses = tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
      }

      writeClasses.configure { task ->
        task.config.convention(extension.toConfig())
      }

      writeClasses
    }
  }
}
