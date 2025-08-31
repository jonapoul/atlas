package modular.gradle

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.internal.impldep.kotlinx.serialization.Serializable
import javax.inject.Inject

abstract class ModuleType @Inject constructor(val name: String) {
  // Optional - defaults to grey
  @get:Input abstract val color: Property<String>

  // Exactly one of these is required
  @get:Input abstract val pathContains: Property<String>
  @get:Input abstract val pathMatches: Property<Regex>
  @get:Input abstract val hasPluginId: Property<String>

  init {
    color.convention("#FFFFFF") // white
    pathContains.unsetConvention()
    pathMatches.unsetConvention()
    hasPluginId.unsetConvention()
  }
}

@Serializable
data class ModuleTypeModel(
  val name: String,
  val color: String,
) : java.io.Serializable
