package modular.d2.tasks

import modular.core.internal.MODULAR_TASK_GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.process.ExecOperations
import org.gradle.work.DisableCachingByDefault
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@DisableCachingByDefault
abstract class InstallD2 : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val scriptFile: RegularFileProperty
  @get:Inject abstract val execOperations: ExecOperations

  init {
    group = MODULAR_TASK_GROUP
    description = "Installs D2 executable, if it's not already installed"
  }

  @TaskAction
  fun execute() {
    val scriptFile = scriptFile.get().asFile
    scriptFile.setExecutable(true)

    val errorBuffer = ByteArrayOutputStream()
    val outputBuffer = ByteArrayOutputStream()
    val command = scriptFile.absolutePath

    logger.lifecycle("Installing d2...")

    val result = execOperations.exec { spec ->
      spec.errorOutput = errorBuffer
      spec.standardOutput = outputBuffer
      spec.isIgnoreExitValue = true
      spec.commandLine(scriptFile.absolutePath)
    }

    if (result.exitValue != 0) {
      throw GradleException("Error code ${result.exitValue} running '$command':\n $errorBuffer")
    }

    logger.info("outputBuffer = '$outputBuffer'")
    logger.info("errorBuffer = '$errorBuffer'")
  }

  internal companion object {
    private const val NAME = "installD2"

    internal fun get(target: Project): TaskProvider<InstallD2> =
      target.tasks.named(NAME, InstallD2::class.java)

    internal fun register(
      target: Project,
      downloadD2Installer: TaskProvider<DownloadD2Installer>,
    ): TaskProvider<InstallD2> = with(target) {
      tasks.register(NAME, InstallD2::class.java) { task ->
        task.scriptFile.convention(downloadD2Installer.map { it.outputFile.get() })
      }
    }
  }
}
