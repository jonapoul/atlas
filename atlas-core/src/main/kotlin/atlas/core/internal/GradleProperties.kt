package atlas.core.internal

import atlas.core.IntEnum
import atlas.core.InternalAtlasApi
import atlas.core.StringEnum
import org.gradle.api.Project
import org.gradle.api.provider.Provider

@InternalAtlasApi
public class CoreGradleProperties(override val project: Project) : IGradleProperties {
  public val alsoTraverseUpwards: Provider<Boolean> = bool("atlas.alsoTraverseUpwards", default = false)
  public val checkOutputs: Provider<Boolean> = bool(key = "atlas.checkOutputs", default = true)
  public val displayLinkLabels: Provider<Boolean> = bool(key = "atlas.addLinkLabels", default = false)
  public val generateOnSync: Provider<Boolean> = bool(key = "atlas.generateOnSync", default = false)
  public val groupProjects: Provider<Boolean> = bool(key = "atlas.groupProjects", default = false)
  public val printFilesToConsole: Provider<Boolean> = bool(key = "atlas.printFilesToConsole", default = false)
}

@InternalAtlasApi
public interface IGradleProperties {
  public val project: Project
}

@InternalAtlasApi
public fun IGradleProperties.bool(key: String, default: Boolean? = null): Provider<Boolean> =
  prop(key, default, String::toBooleanStrict)

@InternalAtlasApi
public fun IGradleProperties.float(key: String, default: Float? = null): Provider<Float> =
  prop(key, default, String::toFloat)

@InternalAtlasApi
public fun IGradleProperties.int(key: String, default: Int? = null): Provider<Int> =
  prop(key, default, String::toInt)

@InternalAtlasApi
public fun IGradleProperties.string(key: String, default: String? = null): Provider<String> =
  prop(key, default) { it }

@InternalAtlasApi
public inline fun <reified E> IGradleProperties.enum(
  key: String,
  default: E? = null,
): Provider<E> where E : StringEnum, E : Enum<E> = string(key, default?.string).map { parseEnum(it) }

@InternalAtlasApi
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
