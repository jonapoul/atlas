package atlas.core.internal

import atlas.core.InternalAtlasApi

@InternalAtlasApi
public fun <T> Map<String, T>.sortedByKeys(): List<Pair<String, T>> = toList().sortedBy { (k, _) -> k }
