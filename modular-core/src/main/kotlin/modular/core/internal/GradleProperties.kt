/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import org.gradle.api.Project
import org.gradle.api.provider.Provider

@InternalModularApi
class CoreGradleProperties(override val project: Project) : IGradleProperties {
  val alsoTraverseUpwards: Provider<Boolean> = bool("modular.alsoTraverseUpwards", default = false)
  val checkOutputs: Provider<Boolean> = bool(key = "modular.checkOutputs", default = true)
  val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  val groupModules: Provider<Boolean> = bool(key = "modular.groupModules", default = false)
  val printFilesToConsole: Provider<Boolean> = bool(key = "modular.printFilesToConsole", default = false)
  val separator: Provider<String> = string(key = "modular.separator", default = ",")
}

@InternalModularApi
interface IGradleProperties {
  val project: Project
}

@InternalModularApi
fun IGradleProperties.bool(key: String, default: Boolean? = null) = prop(key, default, String::toBooleanStrict)

@InternalModularApi
fun IGradleProperties.float(key: String, default: Float? = null) = prop(key, default, String::toFloat)

@InternalModularApi
fun IGradleProperties.int(key: String, default: Int? = null) = prop(key, default, String::toInt)

@InternalModularApi
fun IGradleProperties.string(key: String, default: String? = null) = prop(key, default) { it }

private inline fun <reified T : Any> IGradleProperties.prop(
  key: String,
  default: T?,
  noinline mapper: (String) -> T?,
) = project.providers
  .gradleProperty(key)
  .map(mapper)
  .orElse(project.provider { default })
