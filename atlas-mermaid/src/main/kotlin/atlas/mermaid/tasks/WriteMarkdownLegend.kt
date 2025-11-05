package atlas.mermaid.tasks

import atlas.core.LinkType
import atlas.core.ProjectType
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.Variant.Legend
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.logIfConfigured
import atlas.core.internal.orderedLinkTypes
import atlas.core.internal.orderedProjectTypes
import atlas.core.internal.outputFile
import atlas.core.internal.projectType
import atlas.core.internal.qualifier
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
 * Generates a legend table in markdown format, since Mermaid itself doesn't support tables.
 */
@CacheableTask
public abstract class WriteMarkdownLegend : DefaultTask(), TaskWithOutputFile, AtlasGenerationTask {
  @get:Input public abstract val projectTypes: ListProperty<ProjectType>
  @get:Input public abstract val linkTypes: SetProperty<LinkType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates the legend for a project dependency graph"
  }

  @TaskAction
  public open fun execute() {
    val projectTypes = projectTypes.get()
    val linkTypes = linkTypes.get()
    val outputFile = outputFile.get().asFile

    val hasProjectTypes = projectTypes.isNotEmpty()
    val hasLinkTypes = linkTypes.isNotEmpty()

    val contents = buildString {
      if (hasProjectTypes) {
        appendProjectTypesTable(projectTypes)
      }

      if (hasLinkTypes) {
        if (hasProjectTypes) appendLine()
        appendLinkTypesTable(linkTypes)
      }
    }

    outputFile.writeText(contents)
    logIfConfigured(outputFile)
  }

  private fun StringBuilder.appendProjectTypesTable(projectTypes: List<ProjectType>) {
    appendLine("| Project Types | Color |")
    appendLine("|:--:|:--:|")

    for (type in projectTypes) {
      val value = type.color?.let { color ->
        val url = "https://img.shields.io/badge/-%20-${parseColor(color)}?style=flat-square"
        "<img src=\"$url\" height=\"30\" width=\"100\">"
      } ?: "<no color>"

      appendLine("| ${type.name} | $value |")
    }
  }

  private fun StringBuilder.appendLinkTypesTable(linkTypes: Set<LinkType>) {
    appendLine("| Link Types | Style |")
    appendLine("|:--:|:--:|")

    for (type in linkTypes) {
      val style = listOfNotNull(type.color, type.style).joinToString(separator = " ") { it.capitalized() }
      appendLine("| ${type.displayName} | $style |")
    }
  }

  private fun parseColor(color: String) = if (color.matches("#[0-9A-Fa-f]{6}".toRegex())) {
    // hex color, e.g. "#ABC123" -> "ABC123"
    color.removePrefix("#")
  } else {
    // assume it's a valid color name, e.g. "orange"
    color
  }

  @DisableCachingByDefault
  internal abstract class WriteMarkdownLegendDummy : WriteMarkdownLegend(), DummyAtlasGenerationTask

  internal companion object {
    private const val TASK_NAME = "writeMermaidLegend"

    internal fun get(target: Project): TaskProvider<WriteMarkdownLegend> =
      target.tasks.named(TASK_NAME, WriteMarkdownLegend::class.java)

    internal fun real(
      target: Project,
      extension: AtlasExtensionImpl,
    ) = register<WriteMarkdownLegend>(
      target,
      extension,
      outputFile = target.outputFile(Legend, fileExtension = "md"),
    )

    internal fun dummy(
      target: Project,
      extension: AtlasExtensionImpl,
    ) = register<WriteMarkdownLegendDummy>(
      target = target,
      extension = extension,
      outputFile = target.atlasBuildDirectory
        .get()
        .file("legend-temp.md")
        .asFile,
    )

    private inline fun <reified T : WriteMarkdownLegend> register(
      target: Project,
      extension: AtlasExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val name = "write${T::class.qualifier}MermaidLegend"
      val writeLegend = tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
      }

      writeLegend.configure { task ->
        task.projectTypes.convention(extension.orderedProjectTypes().map(::projectType))
        task.linkTypes.convention(extension.orderedLinkTypes())
      }

      return writeLegend
    }
  }
}
