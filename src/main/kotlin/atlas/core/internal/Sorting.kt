package atlas.core.internal

internal fun <T> Map<String, T>.sortedByKeys(): List<Pair<String, T>> =
  toList().sortedBy { (k, _) -> k }
