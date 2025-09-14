/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.ModularExtension
import modular.graphviz.spec.GraphVizSpec
import modular.spec.ModuleType
import modular.spec.ModuleTypeModel
import modular.spec.OutputSpec
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.ModularGenerationTask
import modular.tasks.TaskWithSeparator
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty

internal fun ModularExtension.orderedTypes(): List<ModuleType> =
  (moduleTypes as OrderedNamedContainer<ModuleType>).getInOrder()

internal fun ModularExtensionImpl.graphVizSpec(): GraphVizSpec =
  specs
    .filterIsInstance<GraphVizSpec>()
    .firstOrNull()
    ?: error("No graphViz spec found")

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

internal fun ObjectFactory.string(convention: String): Property<String> =
  property(String::class.java).convention(convention)

internal inline fun <reified T : Any> ObjectFactory.set(convention: Set<T>): SetProperty<T> =
  setProperty(T::class.java).convention(convention)

internal fun ObjectFactory.directory(convention: Directory): DirectoryProperty =
  directoryProperty().convention(convention)

internal inline fun <reified E : Enum<E>> ObjectFactory.enum(convention: Provider<E>): Property<E> =
  property(E::class.java).convention(convention)

internal fun Project.configureSeparators(extension: ModularExtension) {
  tasks.withType(TaskWithSeparator::class.java).configureEach { t ->
    t.separator.convention(extension.separator)
  }
}

internal fun Project.registerGenerationTaskOnSync(extension: ModularExtension) {
  afterEvaluate {
    val isGradleSync = System.getProperty("idea.sync.active") == "true"

    if (extension.generateOnSync.get() && isGradleSync) {
      val modularGenerationTasks = tasks.withType(ModularGenerationTask::class.java)
      tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(modularGenerationTasks)
    }
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

internal fun StringBuilder.appendIndented(value: Any) = append("  $value")

internal fun StringBuilder.appendIndentedLine(value: Any) {
  appendIndented(value)
  appendLine()
}

internal fun Project.outputFile(
  output: OutputSpecImpl,
  variant: Variant,
  fileExtension: String,
): RegularFile {
  val baseName = baseName(output, variant).get()
  val relativeToRoot = configuredOutputDir(output, variant).file("$baseName.$fileExtension")
  val relativeToSubmodule = relativeToRoot
    .get()
    .asFile
    .relativeTo(rootProject.projectDir)
    .path
  return layout.projectDirectory.file(relativeToSubmodule)
}

private fun baseName(output: OutputSpec, variant: Variant) = when (variant) {
  Variant.Modules -> output.chartRootFilename
  Variant.Legend -> output.legendRootFilename
}

private fun configuredOutputDir(output: OutputSpecImpl, variant: Variant) = when (variant) {
  Variant.Modules -> output.chartOutputDirectory
  Variant.Legend -> output.legendOutputDirectory
}
