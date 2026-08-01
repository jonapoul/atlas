package atlas.core.internal

import atlas.core.ProjectType
import java.io.File
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

@KSerializable
internal data class TypedProject(
  val projectPath: String,
  val type: ProjectType?,
) : JSerializable, Comparable<TypedProject> {
  override fun compareTo(other: TypedProject): Int = projectPath.compareTo(other.projectPath)
}

internal fun readProjectTypes(inputFile: File): Set<TypedProject> =
  inputFile.inputStream().use { stream ->
    AtlasJson.decodeFromStream(
      deserializer = SetSerializer(TypedProject.serializer()),
      stream = stream,
    )
  }

internal fun readProjectType(inputFile: File): TypedProject =
  inputFile.inputStream().use { stream ->
    AtlasJson.decodeFromStream(
      deserializer = TypedProject.serializer(),
      stream = stream,
    )
  }

internal fun writeProjectTypes(projects: Collection<TypedProject>, outputFile: File) {
  outputFile.outputStream().use { stream ->
    AtlasJson.encodeToStream(
      serializer = ListSerializer(TypedProject.serializer()),
      stream = stream,
      value = projects.sorted(),
    )
  }
}

internal fun writeProjectType(project: TypedProject, outputFile: File) {
  outputFile.outputStream().use { stream ->
    AtlasJson.encodeToStream(
      serializer = TypedProject.serializer(),
      value = project,
      stream = stream,
    )
  }
}
