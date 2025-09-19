package modular.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    plugins {
      id("org.jetbrains.kotlin.jvm")
      id("org.jetbrains.kotlinx.binary-compatibility-validator")
      id("idea")
    }

    val javaInt = properties["modular.javaVersion"]?.toString()?.toInt()
      ?: error("Failed getting java version from $properties")
    val javaVersion = JavaVersion.toVersion(javaInt)

    extensions.configure<JavaPluginExtension> {
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }

    extensions.configure<KotlinJvmProjectExtension> {
      jvmToolchain(javaInt)
    }

    extensions.configure<IdeaModel> {
      module {
        isDownloadJavadoc = true
        isDownloadSources = true
      }
    }

    dependencies {
      // TBC
    }
  }
}
