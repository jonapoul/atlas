/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.spec.LinkType
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import java.io.File
import java.util.SortedMap
import kotlin.reflect.typeOf

internal object ModuleLinks {
  fun of(
    project: Project,
    ignoredConfigs: Collection<String>,
  ): Provider<Map<String, List<String>>> = project.provider {
    val map = hashMapOf<String, List<String>>()
    project
      .configurations
      .filterUseful(ignoredConfigs)
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
    linkTypes: Set<LinkType>,
    separator: String,
  ): SortedMap<String, List<String>> {
    val links = moduleLinks.toSortedMap()
    outputFile.writeText(
      buildString {
        links.forEach { (toPath, configurations) ->
          configurations
            .sorted()
            .forEach { config ->
              val type = linkTypes.firstOrNull { it.configuration.matches(config) }
              val link = ModuleLink(fromPath, toPath, config, type?.style, type?.color)
              appendLine(link.string(separator))
            }
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
  val style: String?,
  val color: String?,
) : Comparable<ModuleLink> {
  override fun compareTo(other: ModuleLink): Int =
    fromPath.compareTo(other.fromPath).takeIf { it != 0 }
      ?: toPath.compareTo(other.toPath).takeIf { it != 0 }
      ?: configuration.compareTo(other.configuration)

  fun string(separator: String): String = listOf(fromPath, toPath, configuration, style, color)
    .joinToString(separator) { it.orEmpty() }
}

internal operator fun Set<ModuleLink>.contains(module: TypedModule): Boolean =
  any { (from, to, _) -> from == module.projectPath || to == module.projectPath }

private fun ModuleLink(line: String, separator: String): ModuleLink {
  val (fromPath, toPath, configuration, style, color) = line.split(separator)
  return ModuleLink(
    fromPath = fromPath,
    toPath = toPath,
    configuration = configuration,
    style = style.ifEmpty { null },
    color = color.ifEmpty { null },
  )
}

private fun ConfigurationContainer.filterUseful(ignoredConfigs: Collection<String>) = filter { c ->
  ignoredConfigs.none { blocked ->
    c.name.contains(blocked, ignoreCase = true)
  }
}
