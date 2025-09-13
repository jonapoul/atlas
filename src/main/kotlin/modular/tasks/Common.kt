/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

internal const val MODULAR_TASK_GROUP = "modular"

interface TaskWithSeparator : Task {
  @get:Input val separator: Property<String>
}

interface TaskWithOutputFile : Task {
  @get:OutputFile val outputFile: RegularFileProperty
}

/**
 * Just so we can easily grab all instances of the top-level tasks from this plugin. See
 * [modular.internal.registerGenerationTaskOnSync]
 */
interface ModularGenerationTask : Task
