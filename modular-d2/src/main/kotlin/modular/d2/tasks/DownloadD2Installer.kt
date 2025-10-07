package modular.d2.tasks

import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.modularBuildDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import java.net.URI

@CacheableTask
abstract class DownloadD2Installer : DefaultTask() {
  @get:Input abstract val scriptUrl: Property<String>
  @get:OutputFile abstract val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Downloads D2 installer script"
  }

  @TaskAction
  fun execute() {
    val outputFile = outputFile.get().asFile
    val scriptUrl = scriptUrl.get()

    outputFile.parentFile.mkdirs()

    logger.lifecycle("Downloading D2 install script...")

    URI(scriptUrl).toURL().openStream().use { input ->
      outputFile.outputStream().use { output ->
        output.write(input.readBytes())
      }
    }

    outputFile.setExecutable(true)
  }

  internal companion object {
    private const val NAME = "downloadD2Installer"

    internal fun register(
      target: Project,
    ): TaskProvider<DownloadD2Installer> = with(target) {
      tasks.register(NAME, DownloadD2Installer::class.java) { task ->
        task.scriptUrl.convention("https://d2lang.com/install.sh")
        task.outputFile.convention(modularBuildDirectory.map { it.file("install.sh") })
      }
    }
  }
}
