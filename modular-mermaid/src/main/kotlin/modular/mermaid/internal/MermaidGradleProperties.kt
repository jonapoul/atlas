package modular.mermaid.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.string
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class MermaidGradleProperties(override val project: Project) : IGradleProperties {
  val animateLinks: Provider<Boolean> = bool(key = "modular.mermaid.chart.animateLinks", default = false)
  val look: Provider<String> = string(key = "modular.mermaid.chart.look", default = null)
  val theme: Provider<String> = string(key = "modular.mermaid.chart.theme", default = null)
}
