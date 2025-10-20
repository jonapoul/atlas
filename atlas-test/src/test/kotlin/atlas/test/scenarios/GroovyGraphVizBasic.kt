package atlas.test.scenarios

import atlas.test.Scenario

internal object GroovyGraphVizBasic : Scenario by GroovyBasic {
  override val isGroovy = true

  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.atlas.graphviz'
    }
  """.trimIndent()
}
