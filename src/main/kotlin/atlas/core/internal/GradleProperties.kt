package atlas.core.internal

import atlas.core.IntEnum
import atlas.core.StringEnum
import blueprint.core.floatProperty
import blueprint.core.intProperty
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class CoreGradleProperties(override val project: Project) : IGradleProperties {
  val alsoTraverseUpwards: Provider<Boolean> = bool("atlas.alsoTraverseUpwards", default = false)
  val checkOutputs: Provider<Boolean> = bool(key = "atlas.checkOutputs", default = true)
  val displayLinkLabels: Provider<Boolean> = bool(key = "atlas.addLinkLabels", default = false)
  val generateOnSync: Provider<Boolean> = bool(key = "atlas.generateOnSync", default = false)
  val groupProjects: Provider<Boolean> = bool(key = "atlas.groupProjects", default = false)
  val printFilesToConsole: Provider<Boolean> =
    bool(key = "atlas.printFilesToConsole", default = false)
}

internal interface IGradleProperties {
  val project: Project
}

internal fun IGradleProperties.bool(key: String, default: Boolean? = null): Provider<Boolean> =
  prop(key, default, String::toBooleanStrict)

internal fun IGradleProperties.float(key: String, default: Float? = null): Provider<Float> =
  project.providers.floatProperty(key).orElse(project.provider { default })

internal fun IGradleProperties.int(key: String, default: Int? = null): Provider<Int> =
  project.providers.intProperty(key).orElse(project.provider { default })

internal fun IGradleProperties.string(key: String, default: String? = null): Provider<String> =
  prop(key, default) { it }

internal inline fun <reified E> IGradleProperties.enum(
  key: String,
  default: E? = null,
): Provider<E> where E : StringEnum, E : Enum<E> =
  string(key, default?.string).map { parseEnum(it) }

internal inline fun <reified E> IGradleProperties.intEnum(
  key: String,
  default: E? = null,
): Provider<E> where E : IntEnum, E : Enum<E> = int(key, default?.value).map { parseEnum(it) }

private inline fun <reified T : Any> IGradleProperties.prop(
  key: String,
  default: T?,
  noinline mapper: (String) -> T?,
) = project.providers.gradleProperty(key).map(mapper).orElse(project.provider { default })
