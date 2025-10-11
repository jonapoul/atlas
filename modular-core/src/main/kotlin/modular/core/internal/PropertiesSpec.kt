/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.PropertiesSpec
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import kotlin.reflect.KProperty

@InternalModularApi
class PropertiesSpecImpl(objects: ObjectFactory) : PropertiesSpec {
  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)
}

@InternalModularApi
class Delegate<T>(
  private val mapProperty: MapProperty<String, String>,
  private val key: String,
  private val fromString: (String) -> T,
  private val toString: (T?) -> String? = { it?.toString() },
) {
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) =
    toString(value)?.let { mapProperty.put(key, it) }

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T? =
    mapProperty.get()[key]?.let(fromString)
}

@InternalModularApi
fun PropertiesSpec.bool(key: String): Delegate<Boolean> =
  Delegate(properties, key, fromString = { it.toBoolean() })

@InternalModularApi
inline fun <reified E> PropertiesSpec.enum(key: String): Delegate<E> where E : StringEnum, E : Enum<E> =
  Delegate(
    mapProperty = properties,
    key = key,
    fromString = { str -> enumValues<E>().first { it.string == str } },
    toString = { it?.string },
  )

@InternalModularApi
fun PropertiesSpec.int(key: String): Delegate<Int> =
  Delegate(properties, key, fromString = { Integer.valueOf(it) })

@InternalModularApi
fun PropertiesSpec.float(key: String): Delegate<Float> =
  Delegate(properties, key, fromString = { it.toFloat() })

@InternalModularApi
fun PropertiesSpec.string(key: String): Delegate<String> =
  Delegate(properties, key, fromString = { it })

@InternalModularApi
fun PropertiesSpec.number(key: String): Delegate<Number> =
  Delegate(
    mapProperty = properties,
    key = key,
    fromString = { if (it.contains(".")) it.toFloat() else it.toInt() },
  )
