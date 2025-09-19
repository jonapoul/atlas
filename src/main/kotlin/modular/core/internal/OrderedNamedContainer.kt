/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import groovy.lang.Closure
import modular.spec.ModuleType
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.model.ObjectFactory
import java.util.function.IntFunction

internal class ModuleTypeContainer(objects: ObjectFactory) : OrderedNamedContainer<ModuleType>(
  container = objects.domainObjectContainer(ModuleType::class.java) { name ->
    objects.newInstance(ModuleTypeImpl::class.java, name)
  },
)

internal open class OrderedNamedContainer<T : Any>(
  private val container: NamedDomainObjectContainer<T>,
) : NamedDomainObjectContainer<T> by container {
  private val orderedNames = mutableSetOf<String>()

  override fun register(name: String, configurationAction: Action<in T>): NamedDomainObjectProvider<T> {
    orderedNames.add(name)
    return container.register(name, configurationAction)
  }

  override fun register(name: String): NamedDomainObjectProvider<T> {
    orderedNames.add(name)
    return container.register(name)
  }

  override fun create(name: String): T {
    orderedNames.add(name)
    return container.create(name)
  }

  override fun create(name: String, configureAction: Action<in T>): T {
    orderedNames.add(name)
    return container.create(name, configureAction)
  }

  override fun create(name: String, configureClosure: Closure<*>): T {
    orderedNames.add(name)
    return container.create(name, configureClosure)
  }

  override fun maybeCreate(name: String): T {
    orderedNames.add(name)
    return container.maybeCreate(name)
  }

  fun getInOrder(): List<T> = orderedNames.map(container::getByName)

  // No idea what this is for but if I don't have it I get a linting warning on the interface delegation at the top
  @Suppress("OVERRIDE_DEPRECATION", "RedundantOverride", "DEPRECATION")
  override fun <T : Any?> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? =
    super.toArray(generator)
}
