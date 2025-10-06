/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import modular.core.InternalModularApi
import modular.core.LinkType
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import java.io.File
import kotlin.text.RegexOption.IGNORE_CASE
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@InternalModularApi
fun readModuleLinks(inputFile: File): Set<ModuleLink> = inputFile.inputStream().use { stream ->
  ModularJson.decodeFromStream(
    deserializer = SetSerializer(ModuleLink.serializer()),
    stream = stream,
  )
}

internal fun writeModuleLinks(links: Collection<ModuleLink>, outputFile: File) {
  outputFile.outputStream().use { stream ->
    ModularJson.encodeToStream(
      serializer = ListSerializer(ModuleLink.serializer()),
      stream = stream,
      value = links.sorted(),
    )
  }
}

internal fun writeModuleLinks(
  outputFile: File,
  fromPath: String,
  moduleLinks: Map<String, List<String>>,
  linkTypes: Set<LinkType>,
): List<ModuleLink> {
  val links = moduleLinks.toSortedMap()
  val filteredLinks = mutableListOf<ModuleLink>()
  links.forEach { (toPath, configurations) ->
    configurations
      .sorted()
      .forEach { config ->
        val type = linkTypes.firstOrNull { t ->
          // treat the given string first as an exact match, then fall back to treating as regex
          t.configuration == config || t.configuration.toRegex(IGNORE_CASE).matches(config)
        }
        filteredLinks += ModuleLink(fromPath, toPath, config, type)
      }
  }
  writeModuleLinks(filteredLinks, outputFile)
  return filteredLinks
}

internal fun createModuleLinks(
  project: Project,
  ignoredConfigs: Iterable<String>,
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

@InternalModularApi
@KSerializable
data class ModuleLink(
  val fromPath: String,
  val toPath: String,
  val configuration: String,
  val type: LinkType?,
) : JSerializable, Comparable<ModuleLink> {
  override fun compareTo(other: ModuleLink): Int =
    fromPath.compareTo(other.fromPath).takeIf { it != 0 }
      ?: toPath.compareTo(other.toPath).takeIf { it != 0 }
      ?: configuration.compareTo(other.configuration)
}

@InternalModularApi
operator fun Iterable<ModuleLink>.contains(module: TypedModule): Boolean =
  any { (from, to, _) -> from == module.projectPath || to == module.projectPath }

private fun ConfigurationContainer.filterUseful(ignoredConfigs: Iterable<String>) = filter { c ->
  ignoredConfigs.none { blocked ->
    c.name.contains(blocked, ignoreCase = true)
  }
}
