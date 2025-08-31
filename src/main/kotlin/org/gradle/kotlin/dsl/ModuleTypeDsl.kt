package org.gradle.kotlin.dsl

import modular.gradle.ModuleType
import org.gradle.api.NamedDomainObjectContainer

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.androidApp(
  color: String = "#09AD35", // darker green
) = create("Android App") { type ->
  type.color.set(color)
  type.hasPluginId.set("com.android.application")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.androidLibrary(
  color: String = "#9D8DF1", // light green
): ModuleType = create("Android Library") { type ->
  type.color.set(color)
  type.hasPluginId.set("com.android.library")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.java(
  color: String = "#FF8800", // orange
): ModuleType = create("Java") { type ->
  type.color.set(color)
  type.hasPluginId.set("java")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.kotlinJvm(
  color: String = "#CA66FF", // lavender
): ModuleType = create("Kotlin JVM") { type ->
  type.color.set(color)
  type.hasPluginId.set("org.jetbrains.kotlin.jvm")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.kotlinMultiplatform(
  color: String = "#9D8DF1", // lilac
): ModuleType = create("Kotlin Multiplatform") { type ->
  type.color.set(color)
  type.hasPluginId.set("org.jetbrains.kotlin.multiplatform")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.other(
  color: String = "#808080", // grey
): ModuleType = create("Other") { type ->
  type.color.set(color)
  type.pathContains.set("")
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.builtIns() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}

@DslMarker
annotation class ModularDsl
