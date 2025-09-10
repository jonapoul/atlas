/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.ModularExtension
import modular.spec.ModuleType
import modular.spec.ModuleTypeModel
import modular.tasks.TaskWithSeparator
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty

internal fun ModularExtension.orderedTypes(): List<ModuleType> =
  (moduleTypes as OrderedNamedContainer<ModuleType>).getInOrder()

internal fun moduleTypeModel(type: ModuleType) = ModuleTypeModel(
  name = type.name,
  color = type.color.get(),
)

internal fun ObjectFactory.bool(convention: Provider<Boolean>): Property<Boolean> =
  property(Boolean::class.java).convention(convention)

internal fun ObjectFactory.int(convention: Provider<Int>): Property<Int> =
  property(Int::class.java).convention(convention)

internal fun ObjectFactory.float(convention: Provider<Float>): Property<Float> =
  property(Float::class.java).convention(convention)

internal fun ObjectFactory.string(convention: Provider<String>): Property<String> =
  property(String::class.java).convention(convention)

internal inline fun <reified T : Any> ObjectFactory.set(convention: Set<T>): SetProperty<T> =
  setProperty(T::class.java).convention(convention)

internal inline fun <reified E : Enum<E>> ObjectFactory.enum(convention: E): Property<E> =
  property(E::class.java).convention(convention)

internal inline fun <reified E : Enum<E>> ObjectFactory.enum(convention: Provider<E>): Property<E> =
  property(E::class.java).convention(convention)

internal fun Project.configureSeparators(extension: ModularExtension) {
  tasks.withType(TaskWithSeparator::class.java).configureEach { t ->
    t.separator.convention(extension.separator)
  }
}

internal val Project.outputDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("modular")

internal fun Project.fileInReportDirectory(path: String): Provider<RegularFile> =
  outputDirectory.map { it.file(path) }
