/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.gradle.ModularExtension
import modular.internal.ModularExtensionImpl
import modular.spec.Spec
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

internal const val MODULAR_TASK_GROUP = "modular"

internal interface TaskWithSeparator : Task {
  @get:Input val separator: Property<String>
}

internal interface TaskWithOutputFile : Task {
  @get:OutputFile val outputFile: RegularFileProperty
}

/**
 * Just so we can easily grab all instances of the top-level tasks from this plugin. See [registerGenerationTaskOnSync]
 */
internal interface ModularGenerationTask : Task

internal fun Project.registerGenerationTaskOnSync(extension: ModularExtension) {
  afterEvaluate {
    val isGradleSync = System.getProperty("idea.sync.active") == "true"

    if (extension.general.generateOnSync.get() && isGradleSync) {
      val modularGenerationTasks = tasks.withType(ModularGenerationTask::class.java)
      tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(modularGenerationTasks)
    }
  }
}

internal fun defaultOutputFile(
  extension: ModularExtensionImpl,
  spec: Spec,
): Provider<RegularFile> = extension.outputs.legendOutputDirectory.map { dir ->
  val filename = extension.outputs.legendRootFilename.get()
  val fileExtension = spec.fileExtension.get()
  dir.file("$filename.$fileExtension")
}
