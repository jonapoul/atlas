package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.LinkTypeSpec
import atlas.core.ProjectTypeSpec
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedProjectTypeContainer
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import java.util.function.IntFunction

@InternalAtlasApi
public open class ProjectTypeContainer<T : ProjectTypeSpec>(
  delegate: NamedDomainObjectContainer<T>,
) : OrderedNamedContainer<T>(delegate), NamedProjectTypeContainer<T>

@InternalAtlasApi
public open class LinkTypeContainer<T : LinkTypeSpec>(
  delegate: NamedDomainObjectContainer<T>,
) : OrderedNamedContainer<T>(delegate), NamedLinkTypeContainer<T>

@InternalAtlasApi
public open class OrderedNamedContainer<T : Any>(
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

  public fun getInOrder(): List<T> = orderedNames.map(container::getByName)

  // No idea what this is for but if I don't have it I get a linting warning on the interface delegation at the top
  @Suppress("OVERRIDE_DEPRECATION", "RedundantOverride", "DEPRECATION")
  override fun <T : Any> toArray(generator: IntFunction<Array<out T>>): Array<out T> =
    super.toArray(generator)
}
