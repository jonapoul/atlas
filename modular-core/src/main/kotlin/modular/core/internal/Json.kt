/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import modular.core.InternalModularApi

@InternalModularApi
public val ModularJson: Json = Json {
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
