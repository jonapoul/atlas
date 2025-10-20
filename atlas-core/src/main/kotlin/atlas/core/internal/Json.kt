package atlas.core.internal

import atlas.core.InternalAtlasApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@InternalAtlasApi
public val AtlasJson: Json = Json {
  encodeDefaults = false
  explicitNulls = false
  ignoreUnknownKeys = false
  prettyPrint = false
}

internal object RegexSerializer : KSerializer<Regex> {
  override val descriptor = PrimitiveSerialDescriptor(serialName = "kotlin.text.Regex", kind = PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: Regex) = encoder.encodeString(value.pattern)
  override fun deserialize(decoder: Decoder): Regex = decoder.decodeString().let(::Regex)
}
