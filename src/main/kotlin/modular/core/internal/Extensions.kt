/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.spec.ModuleType
import modular.core.spec.ModuleTypeSpec
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithSeparator
import modular.gradle.ModularExtension
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import java.io.File

internal fun ModularExtension.orderedTypes(): List<ModuleTypeSpec> =
  (moduleTypes as OrderedNamedContainer<ModuleTypeSpec>).getInOrder()

internal fun moduleTypeModel(type: ModuleTypeSpec) = ModuleType(
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

internal fun ObjectFactory.string(convention: String): Property<String> =
  property(String::class.java).convention(convention)

internal inline fun <reified T : Any> ObjectFactory.set(convention: Set<T>): SetProperty<T> =
  setProperty(T::class.java).convention(convention)

internal fun ObjectFactory.directory(convention: Directory): DirectoryProperty =
  directoryProperty().convention(convention)

internal fun Project.configureSeparators(extension: ModularExtension) {
  tasks.withType(TaskWithSeparator::class.java).configureEach { t ->
    t.separator.convention(extension.separator)
  }
}

internal fun Project.configurePrintFilesToConsole(extension: ModularExtension) {
  tasks.withType(ModularGenerationTask::class.java).configureEach { t ->
    t.printFilesToConsole.convention(extension.printFilesToConsole)
  }
}

internal fun Project.registerModularGenerateTask() {
  tasks.register("modularGenerate") { t ->
    t.group = MODULAR_TASK_GROUP
    t.description = "Aggregates all Modular generation tasks"
    t.dependsOn(tasks.withType(ModularGenerationTask::class.java))
  }
}

internal val Project.modularBuildDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("modular")

internal fun Project.fileInBuildDirectory(path: String): Provider<RegularFile> =
  modularBuildDirectory.map { it.file(path) }

internal fun Project.outputFile(extension: ModularExtensionImpl, variant: Variant, fileExtension: String): File {
  val rootName = when (variant) {
    Variant.Chart -> extension.outputs.chartRootFilename
    Variant.Legend -> extension.outputs.legendRootFilename
  }
  val root = when (variant) {
    Variant.Chart -> project.rootDir()
    Variant.Legend -> rootProject.rootDir()
  }
  val hackyDir = when (variant) {
    Variant.Legend -> extension.outputs.legendDir
    Variant.Chart -> extension.outputs.chartDir
  }
  val relative = hackyDir.get().asFile.relativeTo(rootProject.rootDir())
  val actualDir = root.resolve(relative).absoluteFile
  return actualDir.resolve("${rootName.get()}.$fileExtension")
}

private fun Project.rootDir() = layout.projectDirectory.asFile
