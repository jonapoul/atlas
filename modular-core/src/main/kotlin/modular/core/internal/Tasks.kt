/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import java.io.File

@InternalModularApi
const val MODULAR_TASK_GROUP = "modular"

@InternalModularApi
interface TaskWithSeparator : Task {
  @get:Input val separator: Property<String>
}

@InternalModularApi
interface TaskWithOutputFile : Task {
  @get:OutputFile val outputFile: RegularFileProperty
}

@InternalModularApi
interface ModularGenerationTask : Task {
  @get:Input val printFilesToConsole: Property<Boolean>
}

@InternalModularApi
fun ModularGenerationTask.logIfConfigured(file: File) {
  if (printFilesToConsole.get()) {
    logger.lifecycle(file.absolutePath)
  }
}
