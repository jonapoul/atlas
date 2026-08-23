package atlas.test.scenarios

import atlas.test.D2Scenario

/** No `atlasConfig` at all, so the only D2 config is the bare `d2()` call emitted for it. */
internal object GroovyD2Basic : D2Scenario {
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
