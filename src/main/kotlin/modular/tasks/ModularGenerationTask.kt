/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import org.gradle.api.Task

/**
 * Just so we can easily grab all instances of the top-level tasks from this plugin. See
 * [modular.internal.registerGenerationTaskOnSync]
 */
internal sealed interface ModularGenerationTask : Task
