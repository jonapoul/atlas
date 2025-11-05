package atlas.test.scenarios

import atlas.test.GraphvizScenario

internal object GroovyBasic : GraphvizScenario {
  override val isGroovy = true

  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id '$pluginId'
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "a" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
      }

      dependencies {
        api(project(':b'))
        implementation(project(':c'))
      }
    """.trimIndent(),

    "b" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
      }
    """.trimIndent(),

    "c" to """
      plugins {
        id 'java'
      }
    """.trimIndent(),
  )
}
