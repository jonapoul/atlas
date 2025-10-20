package atlas.graphviz.internal

import atlas.core.internal.IndentedStringBuilder

@Suppress("SpreadOperator")
internal class Attrs(private val delegate: MutableMap<String, Any?>) : MutableMap<String, Any?> by delegate {
  constructor(vararg entries: Pair<String, Any?>) : this(mutableMapOf(*entries))

  override fun toString(): String {
    if (isEmpty() || values.all { it == null }) return ""
    val csv = mapNotNull { (k, v) -> if (v == null) null else "$k=\"$v\"" }.joinToString(separator = ",")
    return " [$csv]"
  }

  fun hasAnyValues() = values.any { it != null }

  operator fun plus(other: Map<String, Any?>?): Attrs {
    other?.let(delegate::putAll)
    return Attrs(delegate)
  }
}

internal fun attrs(map: Map<String, String>?) = Attrs(map.orEmpty().toMutableMap())

internal fun IndentedStringBuilder.appendHeaderGroup(name: String, attrs: Attrs) {
  if (!attrs.hasAnyValues()) return
  appendLine("$name$attrs")
}
