/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.LinkType
import modular.core.LinkTypeSpec
import modular.core.ModuleType
import modular.core.ModuleTypeSpec
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import java.io.File

@InternalModularApi
public fun ModularExtensionImpl.orderedModuleTypes(): List<ModuleTypeSpec> = moduleTypes.getInOrder()

@InternalModularApi
public fun ModularExtensionImpl.orderedLinkTypes(): List<LinkType> =
  linkTypes.getInOrder().map(::linkType)

@InternalModularApi
public fun moduleType(type: ModuleTypeSpec): ModuleType = ModuleType(
  name = type.name,
  color = type.color.orNull,
  properties = type.properties.getOrElse(mutableMapOf()),
)

@InternalModularApi
public fun linkType(type: LinkTypeSpec): LinkType = LinkType(
  configuration = type.configuration.get(),
  style = type.style.orNull,
  color = type.color.orNull,
  displayName = type.name,
  properties = type.properties.getOrElse(mutableMapOf()),
)

@InternalModularApi
public fun ObjectFactory.bool(convention: Provider<Boolean>): Property<Boolean> =
  property(Boolean::class.java).convention(convention)

@InternalModularApi
public fun ObjectFactory.int(convention: Provider<Int>): Property<Int> =
  property(Int::class.java).convention(convention)

@InternalModularApi
public fun ObjectFactory.string(convention: Provider<String>): Property<String> =
  property(String::class.java).convention(convention)

@InternalModularApi
public fun ObjectFactory.string(convention: String?): Property<String> =
  property(String::class.java).convention(convention)

@InternalModularApi
public inline fun <reified E> ObjectFactory.enum(convention: E?): Property<E> where E : StringEnum, E : Enum<E> =
  property(E::class.java).convention(convention)

@InternalModularApi
public inline fun <reified E> ObjectFactory.enum(
  convention: Provider<E>,
): Property<E> where E : StringEnum, E : Enum<E> = property(E::class.java).convention(convention)

@InternalModularApi
public inline fun <reified E> ObjectFactory.intEnum(
  convention: Provider<E>,
): Property<E> where E : IntEnum, E : Enum<E> = property(E::class.java).convention(convention)

@InternalModularApi
public inline fun <reified T : Any> ObjectFactory.set(convention: Set<T>): SetProperty<T> =
  setProperty(T::class.java).convention(convention)

@InternalModularApi
public val Project.modularBuildDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("modular")

@InternalModularApi
public fun Project.fileInBuildDirectory(path: String): Provider<RegularFile> =
  modularBuildDirectory.map { it.file(path) }

private const val DIR_NAME = "modular"

@InternalModularApi
public fun Project.outputFile(
  variant: Variant,
  fileExtension: String,
  filename: String = defaultFilename(variant),
): File {
  val directory = when (variant) {
    Variant.Chart -> project.rootDir().resolve(DIR_NAME)
    Variant.Legend -> rootProject.rootDir().resolve(DIR_NAME)
  }
  return directory.resolve("$filename.$fileExtension")
}

private fun defaultFilename(variant: Variant) = when (variant) {
  Variant.Chart -> "chart"
  Variant.Legend -> "legend"
}

private fun Project.rootDir() = layout.projectDirectory.asFile
