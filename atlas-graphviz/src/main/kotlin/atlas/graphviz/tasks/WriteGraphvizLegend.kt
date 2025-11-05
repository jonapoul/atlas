package atlas.graphviz.tasks

import atlas.core.LinkType
import atlas.core.ProjectType
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.Variant.Legend
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.buildIndentedString
import atlas.core.internal.logIfConfigured
import atlas.core.internal.projectType
import atlas.core.internal.orderedLinkTypes
import atlas.core.internal.orderedProjectTypes
import atlas.core.internal.outputFile
import atlas.core.internal.qualifier
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import atlas.graphviz.DotConfig
import atlas.graphviz.GraphvizSpec
import atlas.graphviz.Shape.Plaintext
import atlas.graphviz.internal.appendHeaderGroup
import atlas.graphviz.internal.attrs
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
 * Converts a [DotConfig] into a written legend table, generated once in the project root.
 */
@CacheableTask
public abstract class WriteGraphvizLegend : DefaultTask(), TaskWithOutputFile, AtlasGenerationTask {
  @get:Input public abstract val projectTypes: ListProperty<ProjectType>
  @get:Input public abstract val linkTypes: ListProperty<LinkType>
  @get:Input public abstract val config: Property<DotConfig>
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
    val config = config.get()

    val hasProjectTypes = projectTypes.isNotEmpty()
    val hasLinkTypes = linkTypes.isNotEmpty()

    val dotFileContents = buildIndentedString {
      appendLine("digraph {")
      indent {
        appendHeaderGroup(name = "node", attrs(config.nodeAttributes) + mapOf("shape" to Plaintext))
        appendHeaderGroup(name = "edge", attrs(config.edgeAttributes))
        appendHeaderGroup(name = "graph", attrs(config.graphAttributes))

        val tableAttrs = tableAttributes(config).joinToString(separator = " ") { (k, v) -> "$k=\"$v\"" }

        if (hasProjectTypes) {
          appendLine("projects [label=<")
          appendLine("<TABLE $tableAttrs>")
          appendLine("  <TR><TD COLSPAN=\"2\"><B>Project Types</B></TD></TR>")
          indent {
            projectTypes.forEach { type ->
              val text = withFontColor(text = "&lt;project-name&gt;", properties = type.properties)
              appendLine("<TR><TD>${type.name}</TD><TD BGCOLOR=\"${type.color}\">$text</TD></TR>")
            }
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }

        if (hasLinkTypes) {
          appendLine("links [label=<")
          appendLine("<TABLE $tableAttrs>")
          appendLine("  <TR><TD COLSPAN=\"2\"><B>Link Types</B></TD></TR>")
          linkTypes.forEach { type ->
            val bgColor = if (type.color == null) "" else " BGCOLOR=\"${type.color}\""
            val text = type.style?.capitalized() ?: "Solid"
            val style = withFontColor(text, type.properties)
            appendLine("  <TR><TD>${type.displayName}</TD><TD$bgColor>$style</TD></TR>")
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }
      }

      appendLine("}")
    }

    outputFile.writeText(dotFileContents)
    logIfConfigured(outputFile)
  }

  private fun withFontColor(text: String, properties: Map<String, String>): String {
    val fontColor = properties["fontcolor"] ?: return text
    return "<FONT COLOR=\"$fontColor\">$text</FONT>"
  }

  @Suppress("MagicNumber")
  private fun tableAttributes(config: DotConfig) = listOf(
    "BORDER" to 0,
    "CELLBORDER" to 1,
    "CELLSPACING" to 0,
    "CELLPADDING" to 4,
    "COLOR" to config.fontColor(),
  ).mapNotNull { (k, v) ->
    if (v == null) null else k to v.toString()
  }

  private fun DotConfig.fontColor(): String? =
    listOf(nodeAttributes, graphAttributes).firstNotNullOfOrNull { it?.get("fontcolor") }

  @DisableCachingByDefault
  internal abstract class WriteGraphvizLegendDummy : WriteGraphvizLegend(), DummyAtlasGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      spec: GraphvizSpec,
      extension: AtlasExtensionImpl,
    ) = register<WriteGraphvizLegend>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.outputFile(Legend, spec.fileExtension.get()),
    )

    internal fun dummy(
      target: Project,
      spec: GraphvizSpec,
      extension: AtlasExtensionImpl,
    ) = register<WriteGraphvizLegendDummy>(
      target = target,
      spec = spec,
      extension = extension,
      outputFile = target.atlasBuildDirectory
        .get()
        .file("legend-temp.dot")
        .asFile,
    )

    internal inline fun <reified T : WriteGraphvizLegend> register(
      target: Project,
      spec: GraphvizSpec,
      extension: AtlasExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val name = "write${T::class.qualifier}GraphvizLegend"
      val writeLegend = tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
      }

      writeLegend.configure { task ->
        task.projectTypes.convention(extension.orderedProjectTypes().map(::projectType))
        task.linkTypes.convention(extension.orderedLinkTypes())
        task.config.convention(DotConfig(extension, spec))
      }

      return writeLegend
    }
  }
}
