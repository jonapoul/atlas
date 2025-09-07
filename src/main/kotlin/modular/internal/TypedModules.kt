/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.ModuleTypeModel
import java.io.File

internal object TypedModules {
  fun read(file: File, separator: String): Set<TypedModule> = file
    .readLines()
    .map { TypedModule(it, separator) }
    .toSet()

  fun write(data: Set<TypedModule>, file: File, separator: String) {
    val contents = buildString {
      data.forEach { typedModule ->
        appendLine(typedModule.string(separator))
      }
    }
    file.writeText(contents)
  }
}

internal data class TypedModule(
  val projectPath: String,
  val type: ModuleTypeModel,
  val separator: String,
) : Comparable<TypedModule> {
  fun string(separator: String): String = listOf(projectPath, type.name, type.color).joinToString(separator)
  override fun compareTo(other: TypedModule): Int = projectPath.compareTo(other.projectPath)
}

internal fun TypedModule(string: String, separator: String): TypedModule {
  val (path, name, color) = string.split(separator)
  return TypedModule(path, ModuleTypeModel(name, color), separator)
}
