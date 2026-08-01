package atlas.gradle

import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager

internal fun Project.pluginsInternal(block: PluginManager.() -> Unit) = pluginManager.block()
