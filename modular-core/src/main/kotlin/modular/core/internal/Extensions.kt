/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.spec.LinkType
import modular.core.spec.LinkTypeSpec
import modular.core.spec.ModuleType
import modular.core.spec.ModuleTypeSpec
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import java.io.File

@InternalModularApi
fun ModularExtensionImpl.orderedModuleTypes(): List<ModuleTypeSpec> =
  (moduleTypes as OrderedNamedContainer<ModuleTypeSpec>).getInOrder()

@InternalModularApi
fun ModularExtensionImpl.orderedLinkTypes(): List<LinkTypeSpec> =
  (linkTypes as OrderedNamedContainer<LinkTypeSpec>).getInOrder()

@InternalModularApi
fun moduleType(type: ModuleTypeSpec) = ModuleType(
  name = type.name,
  color = type.color.get(),
)

@InternalModularApi
fun linkType(type: LinkTypeSpec) = LinkType(
  configuration = type.configuration.get(),
  style = type.style.orNull,
  color = type.color.orNull,
  displayName = type.name,
)

@InternalModularApi
fun ObjectFactory.bool(convention: Provider<Boolean>): Property<Boolean> =
  property(Boolean::class.java).convention(convention)

@InternalModularApi
fun ObjectFactory.int(convention: Provider<Int>): Property<Int> =
  property(Int::class.java).convention(convention)

@InternalModularApi
fun ObjectFactory.float(convention: Provider<Float>): Property<Float> =
  property(Float::class.java).convention(convention)

@InternalModularApi
fun ObjectFactory.string(convention: Provider<String>): Property<String> =
  property(String::class.java).convention(convention)

@InternalModularApi
fun ObjectFactory.string(convention: String): Property<String> =
  property(String::class.java).convention(convention)

@InternalModularApi
inline fun <reified T : Any> ObjectFactory.set(convention: Set<T>): SetProperty<T> =
  setProperty(T::class.java).convention(convention)

@InternalModularApi
val Project.modularBuildDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("modular")

@InternalModularApi
fun Project.fileInBuildDirectory(path: String): Provider<RegularFile> =
  modularBuildDirectory.map { it.file(path) }

private const val DIR_NAME = "modular"

@InternalModularApi
fun Project.outputFile(variant: Variant, fileExtension: String): File {
  val directory = when (variant) {
    Variant.Chart -> project.rootDir().resolve(DIR_NAME)
    Variant.Legend -> rootProject.rootDir().resolve(DIR_NAME)
  }
  val filename = when (variant) {
    Variant.Chart -> "chart"
    Variant.Legend -> "legend"
  }
  return directory.resolve("$filename.$fileExtension")
}

private fun Project.rootDir() = layout.projectDirectory.asFile
