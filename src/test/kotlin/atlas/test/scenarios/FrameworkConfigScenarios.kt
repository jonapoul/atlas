package atlas.test.scenarios

import atlas.core.Framework
import atlas.test.KOTLIN_VERSION
import atlas.test.MermaidScenario
import atlas.test.Scenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

/** A project type styled with properties which only D2 and Graphviz know what to do with. */
internal object PropertiesForUnusedFrameworks : MermaidScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm {
          stroke = "black"
          render3D = true
          shadow = true
          peripheries = 2
        }
      }
    }
  """
      .trimIndent()

  override val subprojectBuildFiles = SIMPLE_SUBPROJECTS
}

/** Mermaid has no dotted links, so it'll fall back to dashed ones. */
internal object UnsupportedLinkStyle : MermaidScenario {
  override val rootBuildFile =
    """
    import atlas.core.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      linkTypes {
        api(style = LinkStyle.Dotted)
      }
    }
  """
      .trimIndent()

  override val subprojectBuildFiles = SIMPLE_SUBPROJECTS
}

/** The plugin is applied, but no framework block is configured. */
internal object NoFrameworksConfigured : Scenario {
  override val frameworks = emptySet<Framework>()

  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
      }
    }
  """
      .trimIndent()

  override val subprojectBuildFiles = SIMPLE_SUBPROJECTS
}

private val SIMPLE_SUBPROJECTS: Map<String, String>
  get() =
    mapOf(
      "a" to
        """
      ${MermaidBasic.kotlinJvmBuildScript}
      dependencies {
        api(project(":b"))
      }
    """
          .trimIndent(),
      "b" to MermaidBasic.javaBuildScript,
    )
