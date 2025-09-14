/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.spec.ModuleTypeModel
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
  val type: ModuleTypeModel?,
  val separator: String,
) : Comparable<TypedModule> {
  fun string(separator: String): String = listOfNotNull(
    projectPath,
    type?.name,
    type?.color,
  ).joinToString(separator)

  override fun compareTo(other: TypedModule): Int = projectPath.compareTo(other.projectPath)
}

@Suppress("MagicNumber")
internal fun TypedModule(string: String, separator: String): TypedModule {
  val split = string.split(separator)
  return when (split.size) {
    1 -> TypedModule(projectPath = split[0], type = null, separator)
    3 -> TypedModule(projectPath = split[0], ModuleTypeModel(name = split[1], color = split[2]), separator)
    else -> error("Malformed typed module line: '$string'")
  }
}
