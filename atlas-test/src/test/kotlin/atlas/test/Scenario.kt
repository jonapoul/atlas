/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test

internal interface Scenario {
  val rootBuildFile: String
  val submoduleBuildFiles: Map<String, String> get() = emptyMap()
  val gradlePropertiesFile: String get() = ""
  val isGroovy: Boolean get() = false
  val variant: PluginVariant
  val pluginId get() = "dev.jonpoulton.atlas.$variant"
}

internal interface D2Scenario : Scenario {
  override val variant get() = PluginVariant.D2
}

internal interface GraphvizScenario : Scenario {
  override val variant get() = PluginVariant.Graphviz
}

internal interface MermaidScenario : Scenario {
  override val variant get() = PluginVariant.Mermaid
}

internal enum class PluginVariant(val string: String) {
  D2("d2"),
  Graphviz("graphviz"),
  Mermaid("mermaid"),
  ;

  override fun toString(): String = string
}

internal val Scenario.javaBuildScript
  get() = """
  plugins {
    id("java")
    id("$pluginId")
  }
  """.trimIndent()

internal val Scenario.kotlinJvmBuildScript
  get() = """
  plugins {
    kotlin("jvm")
    id("$pluginId")
  }
  """.trimIndent()

internal val Scenario.androidBuildScript
  get() = """
  plugins {
    kotlin("android")
    id("com.android.library")
    id("$pluginId")
  }

  android {
    namespace = "dev.jonpoulton.dummy.$variant"
    compileSdk = 36
  }
  """.trimIndent()
