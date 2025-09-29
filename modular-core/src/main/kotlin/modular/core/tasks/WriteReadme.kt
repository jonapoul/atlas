/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.InternalModularApi
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularGenerationTask
import modular.core.internal.TaskWithOutputFile
import modular.core.internal.logIfConfigured
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import java.io.File
import kotlin.text.RegexOption.DOT_MATCHES_ALL

@CacheableTask
abstract class WriteReadme : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val chartFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val legendFile: RegularFileProperty
  @get:Internal abstract val readmeFile: RegularFileProperty
  @get:Input abstract val projectPath: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Creates/updates the module README file to insert the modules chart"
  }

  @TaskAction
  fun execute() {
    val readmeFile = readmeFile.asFile.get()

    val newContents = if (readmeFile.exists()) {
      injectInto(readmeFile)
    } else {
      newReadme()
    }

    readmeFile.writeText(newContents)
    logIfConfigured(readmeFile)
  }

  private fun injectInto(file: File): String {
    val contents = file.readText()
    val result = REGION_REGEX.find(contents)
      ?: error("No injectable region found in $file. Requires a block matching regex: $REGION_REGEX")

    val (startRegion, _, endRegion) = result.destructured
    return buildString {
      appendLine(startRegion)
      appendLine()
      appendContents()
      append(endRegion)
    }
  }

  private fun newReadme(): String {
    val expectedTitle = projectPath.get().removePrefix(prefix = ":")
    return buildString {
      appendLine("# $expectedTitle")
      appendLine()
      appendLine(REGION_START)
      appendLine()
      appendContents()
      append(REGION_END)
    }
  }

  private fun StringBuilder.appendContents() {
    val chart = diagramContents(tag = "chart", chartFile.get().asFile)
    if (!chart.isBlank()) appendLine(chart)
    val legend = diagramContents(tag = "legend", legendFile.get().asFile)
    if (!legend.isBlank()) appendLine(legend)
  }

  private fun diagramContents(tag: String, file: File) = when (file.extension.lowercase()) {
    "md" -> buildString {
      file
        .readLines()
        .filter { it.isNotBlank() }
        .onEach { appendLine(it) }
    }

    "mmd" -> buildString {
      appendLine("```mermaid")
      file
        .readLines()
        .filter { it.isNotBlank() }
        .forEach { appendLine(it) }
      appendLine("```")
    }

    else -> {
      val readmeFile = outputFile.get().asFile
      val relativePath = file.relativeTo(readmeFile.parentFile)
      "![$tag]($relativePath)"
    }
  }

  @InternalModularApi
  companion object {
    private const val REGION_START = "<!--region chart-->"
    private const val REGION_END = "<!--endregion-->"
    private val REGION_REGEX = "(.*$REGION_START)(.*?)($REGION_END.*)".toRegex(DOT_MATCHES_ALL)

    @InternalModularApi
    fun <T : TaskWithOutputFile> register(
      target: Project,
      flavor: String,
      chartFile: Provider<RegularFile>,
      legendTask: Provider<T>,
    ): TaskProvider<WriteReadme> = with(target) {
      tasks.register("write${flavor.capitalized()}Readme", WriteReadme::class.java) { task ->
        task.projectPath.convention(target.path)
        task.legendFile.convention(legendTask.map { it.outputFile.get() })
        task.chartFile.convention(chartFile)
        val readme = layout.projectDirectory.file("README.md")
        task.readmeFile.convention(readme)
        task.outputFile.convention(readme)
      }
    }
  }
}
