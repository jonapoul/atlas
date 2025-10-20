@file:Suppress("unused") // public API

package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec
import org.gradle.api.provider.Property

/**
 * Supports future layout engine configurations. Currently only supports [ElkLayoutSpec].
 */
@AtlasDsl
public interface MermaidLayoutSpec : PropertiesSpec {
  public val name: Property<String>
}
