/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi

interface StringEnum {
  val string: String
}

@InternalModularApi
inline fun <reified E> parseEnum(string: String): E where E : StringEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.string == string } ?: error("No ${E::class.simpleName} matching '$string'")

interface IntEnum {
  val value: Int
}

@InternalModularApi
inline fun <reified E> parseEnum(value: Int): E where E : IntEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.value == value } ?: error("No ${E::class.simpleName} matching '$value'")
