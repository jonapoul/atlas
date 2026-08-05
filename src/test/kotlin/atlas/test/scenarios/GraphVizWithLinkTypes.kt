package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizWithLinkTypes : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    linkTypes {
      "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
      api()
      implementation(LinkStyle.Dotted)
    }
    """
      .trimIndent()
}
