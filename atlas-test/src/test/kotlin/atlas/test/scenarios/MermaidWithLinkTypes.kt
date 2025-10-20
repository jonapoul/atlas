package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.MermaidScenario

internal object MermaidWithLinkTypes : MermaidScenario by MermaidBasic {
  override val rootBuildFile = """
    import atlas.mermaid.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      linkTypes {
        api(color = "green")
        implementation(color = "#5555FF")
        "compileOnly"(style = LinkStyle.Dashed, color = "yellow")
      }
    }
  """.trimIndent()
}
