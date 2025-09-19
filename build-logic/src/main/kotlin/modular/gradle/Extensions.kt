package modular.gradle

import org.gradle.api.Project

internal fun Project.plugins(block: PluginScope.() -> Unit) {
  val scope = PluginScope { id -> pluginManager.apply(id) }
  scope.block()
}

internal fun interface PluginScope {
  fun id(id: String)
}
