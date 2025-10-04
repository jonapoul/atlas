/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import org.gradle.api.provider.Provider

interface StringEnum {
  val string: String
}

@InternalModularApi
inline fun <reified E> Provider<String>.mapEnum(): Provider<E> where E : StringEnum, E : Enum<E> =
  map { parseEnum(it) }

@InternalModularApi
inline fun <reified E> parseEnum(string: String): E where E : StringEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.string == string } ?: error("No ${E::class.simpleName} matching '$string'")
