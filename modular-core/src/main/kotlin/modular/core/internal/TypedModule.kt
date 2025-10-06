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
import modular.core.ModuleType
import java.io.File
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@InternalModularApi
@KSerializable
data class TypedModule(
  val projectPath: String,
  val type: ModuleType?,
) : JSerializable, Comparable<TypedModule> {
  override fun compareTo(other: TypedModule): Int = projectPath.compareTo(other.projectPath)
}

@InternalModularApi
fun readModuleTypes(inputFile: File): Set<TypedModule> = inputFile.inputStream().use { stream ->
  ModularJson.decodeFromStream(
    deserializer = SetSerializer(TypedModule.serializer()),
    stream = stream,
  )
}

@InternalModularApi
fun readModuleType(inputFile: File): TypedModule = inputFile.inputStream().use { stream ->
  ModularJson.decodeFromStream(
    deserializer = TypedModule.serializer(),
    stream = stream,
  )
}

internal fun writeModuleTypes(modules: Collection<TypedModule>, outputFile: File) {
  outputFile.outputStream().use { stream ->
    ModularJson.encodeToStream(
      serializer = ListSerializer(TypedModule.serializer()),
      stream = stream,
      value = modules.sorted(),
    )
  }
}

internal fun writeModuleType(module: TypedModule, outputFile: File) {
  outputFile.outputStream().use { stream ->
    ModularJson.encodeToStream(
      serializer = TypedModule.serializer(),
      value = module,
      stream = stream,
    )
  }
}
