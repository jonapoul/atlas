package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.LinkType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import java.io.File
import kotlin.text.RegexOption.IGNORE_CASE
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@InternalAtlasApi
public fun readProjectLinks(inputFile: File): Set<ProjectLink> = inputFile.inputStream().use { stream ->
  AtlasJson.decodeFromStream(
    deserializer = SetSerializer(ProjectLink.serializer()),
    stream = stream,
  )
}

internal fun writeProjectLinks(links: Collection<ProjectLink>, outputFile: File) {
  outputFile.outputStream().use { stream ->
    AtlasJson.encodeToStream(
      serializer = ListSerializer(ProjectLink.serializer()),
      stream = stream,
      value = links.sorted(),
    )
  }
}

internal fun writeProjectLinks(
  outputFile: File,
  fromPath: String,
  projectLinks: Map<String, List<String>>,
  linkTypes: Set<LinkType>,
): List<ProjectLink> {
  val links = projectLinks.toSortedMap()
  val filteredLinks = mutableListOf<ProjectLink>()
  links.forEach { (toPath, configurations) ->
    configurations
      .sorted()
      .forEach { config ->
        val type = linkTypes.firstOrNull { t ->
          // treat the given string first as an exact match, then fall back to treating as regex
          t.configuration == config || t.configuration.toRegex(IGNORE_CASE).matches(config)
        }
        filteredLinks += ProjectLink(fromPath, toPath, config, type)
      }
  }
  writeProjectLinks(filteredLinks, outputFile)
  return filteredLinks
}

internal fun createProjectLinks(
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
        .forEach { p ->
          map[p.path] = map.getOrElse(p.path) { listOf() } + c.name
        }
    }
  map
}

@InternalAtlasApi
@KSerializable
public data class ProjectLink(
  val fromPath: String,
  val toPath: String,
  val configuration: String,
  val type: LinkType?,
) : JSerializable, Comparable<ProjectLink> {
  override fun compareTo(other: ProjectLink): Int =
    fromPath.compareTo(other.fromPath).takeIf { it != 0 }
      ?: toPath.compareTo(other.toPath).takeIf { it != 0 }
      ?: configuration.compareTo(other.configuration)
}

@InternalAtlasApi
public operator fun Iterable<ProjectLink>.contains(p: TypedProject): Boolean =
  any { (from, to, _) -> from == p.projectPath || to == p.projectPath }

private fun ConfigurationContainer.filterUseful(ignoredConfigs: Iterable<String>) = filter { c ->
  ignoredConfigs.none { blocked ->
    c.name.contains(blocked, ignoreCase = true)
  }
}
