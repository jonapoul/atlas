/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.PropertiesSpec
import atlas.core.StringEnum
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import kotlin.reflect.KProperty

@InternalAtlasApi
public class PropertiesSpecImpl(objects: ObjectFactory) : PropertiesSpec {
  override val properties: MapProperty<String, String> = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)
}

@InternalAtlasApi
public class Delegate<T>(
  private val mapProperty: MapProperty<String, String>,
  private val key: String,
  private val fromString: (String) -> T,
  private val toString: (T?) -> String? = { it?.toString() },
) {
  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?): Unit? =
    toString(value)?.let { mapProperty.put(key, it) }

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): T? =
    mapProperty.get()[key]?.let(fromString)
}

@InternalAtlasApi
public fun PropertiesSpec.bool(key: String): Delegate<Boolean> =
  Delegate(properties, key, fromString = { it.toBoolean() })

@InternalAtlasApi
public inline fun <reified E> PropertiesSpec.enum(key: String): Delegate<E> where E : StringEnum, E : Enum<E> =
  Delegate(
    mapProperty = properties,
    key = key,
    fromString = { str -> enumValues<E>().first { it.string == str } },
    toString = { it?.string },
  )

@InternalAtlasApi
public fun PropertiesSpec.int(key: String): Delegate<Int> =
  Delegate(properties, key, fromString = { Integer.valueOf(it) })

@InternalAtlasApi
public fun PropertiesSpec.float(key: String): Delegate<Float> =
  Delegate(properties, key, fromString = { it.toFloat() })

@InternalAtlasApi
public fun PropertiesSpec.string(key: String): Delegate<String> =
  Delegate(properties, key, fromString = { it })

@InternalAtlasApi
public fun PropertiesSpec.number(key: String): Delegate<Number> =
  Delegate(
    mapProperty = properties,
    key = key,
    fromString = { if (it.contains(".")) it.toFloat() else it.toInt() },
  )
