/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import org.gradle.api.Project
import org.gradle.api.provider.Provider

@InternalModularApi
public class CoreGradleProperties(override val project: Project) : IGradleProperties {
  public val alsoTraverseUpwards: Provider<Boolean> = bool("modular.alsoTraverseUpwards", default = false)
  public val checkOutputs: Provider<Boolean> = bool(key = "modular.checkOutputs", default = true)
  public val displayLinkLabels: Provider<Boolean> = bool(key = "modular.addLinkLabels", default = false)
  public val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  public val groupModules: Provider<Boolean> = bool(key = "modular.groupModules", default = false)
  public val printFilesToConsole: Provider<Boolean> = bool(key = "modular.printFilesToConsole", default = false)
}

@InternalModularApi
public interface IGradleProperties {
  public val project: Project
}

@InternalModularApi
public fun IGradleProperties.bool(key: String, default: Boolean? = null): Provider<Boolean> =
  prop(key, default, String::toBooleanStrict)

@InternalModularApi
public fun IGradleProperties.float(key: String, default: Float? = null): Provider<Float> =
  prop(key, default, String::toFloat)

@InternalModularApi
public fun IGradleProperties.int(key: String, default: Int? = null): Provider<Int> =
  prop(key, default, String::toInt)

@InternalModularApi
public fun IGradleProperties.string(key: String, default: String? = null): Provider<String> =
  prop(key, default) { it }

@InternalModularApi
public inline fun <reified E> IGradleProperties.enum(
  key: String,
  default: E? = null,
): Provider<E> where E : StringEnum, E : Enum<E> = string(key, default?.string).map { parseEnum(it) }

@InternalModularApi
public inline fun <reified E> IGradleProperties.intEnum(
  key: String,
  default: E? = null,
): Provider<E> where E : IntEnum, E : Enum<E> = int(key, default?.value).map { parseEnum(it) }

private inline fun <reified T : Any> IGradleProperties.prop(
  key: String,
  default: T?,
  noinline mapper: (String) -> T?,
) = project.providers
  .gradleProperty(key)
  .map(mapper)
  .orElse(project.provider { default })
