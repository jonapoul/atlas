package atlas.mermaid.internal

import atlas.core.internal.IGradleProperties
import atlas.core.internal.bool
import atlas.core.internal.enum
import atlas.mermaid.Look
import atlas.mermaid.Theme
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

internal class MermaidGradleProperties(override val providers: ProviderFactory) :
  IGradleProperties {
  val animateLinks: Provider<Boolean> =
    bool(key = "atlas.mermaid.chart.animateLinks", default = null)
  val look: Provider<Look> = enum(key = "atlas.mermaid.chart.look", default = null)
  val theme: Provider<Theme> = enum(key = "atlas.mermaid.chart.theme", default = null)
}
