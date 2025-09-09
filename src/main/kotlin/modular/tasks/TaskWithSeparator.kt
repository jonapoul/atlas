/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface TaskWithSeparator : Task {
  @get:Input val separator: Property<String>
}
