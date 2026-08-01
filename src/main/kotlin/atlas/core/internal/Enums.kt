package atlas.core.internal

import atlas.core.IntEnum
import atlas.core.StringEnum

internal inline fun <reified E> parseEnum(string: String): E where E : StringEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.string == string }
    ?: error("No ${E::class.simpleName} matching '$string'")

internal inline fun <reified E> parseEnum(value: Int): E where E : IntEnum, E : Enum<E> =
  enumValues<E>().firstOrNull { it.value == value }
    ?: error("No ${E::class.simpleName} matching '$value'")
