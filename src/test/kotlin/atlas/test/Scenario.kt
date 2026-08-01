package atlas.test

import atlas.core.Framework

internal interface Scenario {
  val rootBuildFile: String
  val subprojectBuildFiles: Map<String, String>
    get() = emptyMap()

  val gradlePropertiesFile: String
    get() = ""

  val isGroovy: Boolean
    get() = false

  /**
   * The frameworks this scenario generates diagrams for. [ScenarioTest] switches them on in the
   * generated root build file, so scenarios only need to declare the config they're actually
   * testing.
   */
  val frameworks: Set<Framework>

  val pluginId
    get() = "dev.jonpoulton.atlas"
}

internal interface D2Scenario : Scenario {
  override val frameworks
    get() = setOf(Framework.D2)
}

internal interface GraphvizScenario : Scenario {
  override val frameworks
    get() = setOf(Framework.Graphviz)
}

internal interface MermaidScenario : Scenario {
  override val frameworks
    get() = setOf(Framework.Mermaid)
}

/** The single framework a scenario uses, for tests which assert on generated file paths. */
internal val Scenario.framework: Framework
  get() = frameworks.single()

internal val Scenario.javaBuildScript
  get() =
    """
  plugins {
    id("java")
    id("$pluginId")
  }
  """
      .trimIndent()

internal val Scenario.kotlinJvmBuildScript
  get() =
    """
  plugins {
    kotlin("jvm")
    id("$pluginId")
  }
  """
      .trimIndent()

internal val Scenario.androidBuildScript
  get() =
    """
  plugins {
    id("com.android.library")
    id("$pluginId")
  }

  android {
    namespace = "dev.jonpoulton.dummy.${framework.string}"
    compileSdk = 36
  }
  """
      .trimIndent()
