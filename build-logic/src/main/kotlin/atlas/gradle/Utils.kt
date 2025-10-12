package atlas.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
  get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal operator fun VersionCatalog.invoke(alias: String) = findLibrary(alias).get()

internal fun Project.pluginsInternal(block: PluginManager.() -> Unit) = pluginManager.block()
