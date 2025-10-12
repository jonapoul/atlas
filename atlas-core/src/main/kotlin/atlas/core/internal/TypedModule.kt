/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.ModuleType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@InternalAtlasApi
@KSerializable
public data class TypedModule(
  val projectPath: String,
  val type: ModuleType?,
) : JSerializable, Comparable<TypedModule> {
  override fun compareTo(other: TypedModule): Int = projectPath.compareTo(other.projectPath)
}

@InternalAtlasApi
public fun readModuleTypes(inputFile: File): Set<TypedModule> = inputFile.inputStream().use { stream ->
  AtlasJson.decodeFromStream(
    deserializer = SetSerializer(TypedModule.serializer()),
    stream = stream,
  )
}

@InternalAtlasApi
public fun readModuleType(inputFile: File): TypedModule = inputFile.inputStream().use { stream ->
  AtlasJson.decodeFromStream(
    deserializer = TypedModule.serializer(),
    stream = stream,
  )
}

internal fun writeModuleTypes(modules: Collection<TypedModule>, outputFile: File) {
  outputFile.outputStream().use { stream ->
    AtlasJson.encodeToStream(
      serializer = ListSerializer(TypedModule.serializer()),
      stream = stream,
      value = modules.sorted(),
    )
  }
}

internal fun writeModuleType(module: TypedModule, outputFile: File) {
  outputFile.outputStream().use { stream ->
    AtlasJson.encodeToStream(
      serializer = TypedModule.serializer(),
      value = module,
      stream = stream,
    )
  }
}

@InternalAtlasApi
public object DefaultModuleTypes {
  public const val ANDROID_APP_NAME: String = "Android App"
  public const val ANDROID_APP_COLOR: String = "limegreen"
  public const val ANDROID_LIB_NAME: String = "Android Library"
  public const val ANDROID_LIB_COLOR: String = "lightgreen"
  public const val JAVA_NAME: String = "Java"
  public const val JAVA_COLOR: String = "orange"
  public const val KOTLIN_JVM_NAME: String = "Kotlin JVM"
  public const val KOTLIN_JVM_COLOR: String = "mediumorchid"
  public const val KOTLIN_MP_NAME: String = "Kotlin Multiplatform"
  public const val KOTLIN_MP_COLOR: String = "mediumslateblue"
  public const val OTHER_NAME: String = "Other"
  public const val OTHER_COLOR: String = "gainsboro"
}
