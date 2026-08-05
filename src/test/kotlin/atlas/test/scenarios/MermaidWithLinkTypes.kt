package atlas.test.scenarios

import atlas.test.MermaidScenario

internal object MermaidWithLinkTypes : MermaidScenario by MermaidBasic {
  override val atlasConfig =
    """
    linkTypes {
      api(color = "green")
      implementation(color = "#5555FF")
      "compileOnly"(style = LinkStyle.Dashed, color = "yellow")
    }
    """
      .trimIndent()
}
