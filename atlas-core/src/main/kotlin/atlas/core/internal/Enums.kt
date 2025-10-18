/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.internal

import atlas.core.IntEnum
import atlas.core.InternalAtlasApi
import atlas.core.StringEnum

@InternalAtlasApi
public inline fun <reified E> parseEnum(string: String): E where E : StringEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.string == string } ?: error("No ${E::class.simpleName} matching '$string'")

@InternalAtlasApi
public inline fun <reified E> parseEnum(value: Int): E where E : IntEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.value == value } ?: error("No ${E::class.simpleName} matching '$value'")
