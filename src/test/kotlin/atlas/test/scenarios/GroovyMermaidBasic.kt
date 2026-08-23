package atlas.test.scenarios

import atlas.test.MermaidScenario

/**
 * No `atlasConfig` at all, so the only Mermaid config is the bare `mermaid()` call emitted for it.
 */
internal object GroovyMermaidBasic : MermaidScenario {
  override val isGroovy = true

  override val rootBuildFile =
    """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to
        """
        plugins {
          id 'org.jetbrains.kotlin.jvm'
        }

        dependencies {
          api(project(':b'))
          implementation(project(':c'))
        }
        """
          .trimIndent(),
      "b" to
        """
        plugins {
          id 'org.jetbrains.kotlin.jvm'
        }
        """
          .trimIndent(),
      "c" to
        """
        plugins {
          id 'java'
        }
        """
          .trimIndent(),
    )
}
