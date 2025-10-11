/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi

public interface StringEnum {
  public val string: String
}

@InternalModularApi
public inline fun <reified E> parseEnum(string: String): E where E : StringEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.string == string } ?: error("No ${E::class.simpleName} matching '$string'")

public interface IntEnum {
  public val value: Int
}

@InternalModularApi
public inline fun <reified E> parseEnum(value: Int): E where E : IntEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.value == value } ?: error("No ${E::class.simpleName} matching '$value'")
