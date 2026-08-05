package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object GraphVizChartWithCustomLinkTypes : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    linkTypes {
      api(style = LinkStyle.Bold)
      implementation(color = "blue")
      "compileOnly"(color = "#FF55FF", style = LinkStyle.Dotted)
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to
        """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
        compileOnly(project(":d"))
      }
    """
          .trimIndent(),
      "b" to kotlinJvmBuildScript,
      "c" to javaBuildScript,
      "d" to javaBuildScript,
    )
}
