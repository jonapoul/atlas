package modular.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import java.io.File
import java.util.SortedMap

internal object ModuleLinks {
  fun of(project: Project): Provider<Map<String, List<String>>> = project.provider {
    val map = hashMapOf<String, List<String>>()
    project
      .configurations
      .filterUseful()
      .forEach { c ->
        c.dependencies
          .filterIsInstance<ProjectDependency>()
          .forEach { module ->
            map[module.path] = map.getOrElse(module.path) { listOf() } + c.name
          }
      }
    map
  }

  fun read(inputFile: File, separator: String): Set<ModuleLink> = inputFile
    .readLines()
    .map { ModuleLink(it, separator) }
    .toSet()

  fun write(links: Collection<ModuleLink>, outputFile: File, separator: String) {
    outputFile.printWriter().use { writer ->
      links.sorted().forEach { link ->
        writer.write(link.string(separator))
        writer.write("\n")
      }
    }
  }

  fun write(
    outputFile: File,
    fromPath: String,
    moduleLinks: Map<String, List<String>>,
    separator: String,
  ): SortedMap<String, List<String>> {
    val links = moduleLinks.toSortedMap()
    outputFile.writeText(
      buildString {
        links.forEach { (toPath, configurations) ->
          configurations
            .sorted()
            .forEach { config -> appendLine(listOf(fromPath, toPath, config).joinToString(separator)) }
        }
      },
    )
    return links
  }
}

internal data class ModuleLink(
  val fromPath: String,
  val toPath: String,
  val configuration: String,
  val separator: String,
) : Comparable<ModuleLink> {
  override fun compareTo(other: ModuleLink): Int =
    fromPath.compareTo(other.fromPath).takeIf { it != 0 }
      ?: toPath.compareTo(other.toPath).takeIf { it != 0 }
      ?: configuration.compareTo(other.configuration)

  fun string(separator: String): String = "$fromPath$separator$toPath$separator$configuration"
}

private fun ModuleLink(line: String, separator: String): ModuleLink {
  val (fromPath, toPath, configuration) = line.split(separator)
  return ModuleLink(fromPath, toPath, configuration, separator)
}

// TODO: make configurable
private val BLOCKED_CONFIGS = setOf(
  "debug",
  "kover",
  "ksp",
  "test",
)

private fun ConfigurationContainer.filterUseful() = filter { c ->
  BLOCKED_CONFIGS.none { blocked ->
    c.name.contains(blocked, ignoreCase = true)
  }
}
