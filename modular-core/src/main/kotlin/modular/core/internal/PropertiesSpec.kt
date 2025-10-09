/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.ModularDsl
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import kotlin.reflect.KProperty

@InternalModularApi
@ModularDsl
interface PropertiesSpec {
  val properties: MapProperty<String, String>
  fun put(key: String, value: Any)
}

@InternalModularApi
class PropertiesSpecImpl(objects: ObjectFactory) : PropertiesSpec {
  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)

  override fun put(key: String, value: Any) = properties.put(key, value.toString())
}

@InternalModularApi
class Delegate<T>(
  private val mapProperty: MapProperty<String, String>,
  private val key: String,
  private val fromString: (String) -> T,
  private val toString: (T?) -> String? = { it?.toString() },
) {
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
    mapProperty.put(key, toString(value) ?: return)
  }

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = mapProperty.get()[key]?.let(fromString)
}

@InternalModularApi
fun PropertiesSpec.boolDelegate(key: String): Delegate<Boolean> =
  Delegate(properties, key, fromString = { it.toBoolean() })

@InternalModularApi
inline fun <reified E> PropertiesSpec.enumDelegate(key: String): Delegate<E> where E : StringEnum, E : Enum<E> =
  Delegate(
    mapProperty = properties,
    key = key,
    fromString = { str -> enumValues<E>().first { it.string == str } },
    toString = { it?.string },
  )

@InternalModularApi
fun PropertiesSpec.intDelegate(key: String): Delegate<Int> =
  Delegate(properties, key, fromString = { Integer.valueOf(it) })

@InternalModularApi
fun PropertiesSpec.floatDelegate(key: String): Delegate<Float> =
  Delegate(properties, key, fromString = { it.toFloat() })

@InternalModularApi
fun PropertiesSpec.stringDelegate(key: String): Delegate<String> =
  Delegate(properties, key, fromString = { it })
