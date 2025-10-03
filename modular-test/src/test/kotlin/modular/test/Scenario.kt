/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

interface Scenario {
  val rootBuildFile: String
  val submoduleBuildFiles: Map<String, String> get() = emptyMap()
  val gradlePropertiesFile: String get() = ""
  val isGroovy: Boolean get() = false
  val variant: PluginVariant
  val pluginId get() = "dev.jonpoulton.modular.$variant"
}

interface GraphvizScenario : Scenario {
  override val variant get() = PluginVariant.Graphviz
}

interface MermaidScenario : Scenario {
  override val variant get() = PluginVariant.Mermaid
}

enum class PluginVariant(val string: String) {
  Graphviz("graphviz"),
  Mermaid("mermaid"),
  ;

  override fun toString() = string
}

val Scenario.javaBuildScript
  get() = """
  plugins {
    id("java")
    id("$pluginId")
  }
  """.trimIndent()

val Scenario.kotlinJvmBuildScript
  get() = """
  plugins {
    kotlin("jvm")
    id("$pluginId")
  }
  """.trimIndent()

val Scenario.androidBuildScript
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
