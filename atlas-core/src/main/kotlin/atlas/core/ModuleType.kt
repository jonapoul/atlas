package atlas.core

import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
public data class ModuleType(
  public val name: String,
  public val color: String?,
  public val properties: Map<String, String> = emptyMap(),
) : JSerializable
