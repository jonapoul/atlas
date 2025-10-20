package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.GraphvizScenario
import atlas.test.MermaidScenario

internal object D2ConfigureOnDemand : D2Scenario by D2Basic {
  override val gradlePropertiesFile = """
    org.gradle.configureondemand=true
  """.trimIndent()
}

internal object GraphvizConfigureOnDemand : GraphvizScenario by GraphvizBasic {
  override val gradlePropertiesFile = """
    org.gradle.configureondemand=true
  """.trimIndent()
}

internal object MermaidConfigureOnDemand : MermaidScenario by MermaidBasic {
  override val gradlePropertiesFile = """
    org.gradle.configureondemand=true
  """.trimIndent()
}
