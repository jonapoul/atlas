/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.ModularExtensionImpl
import modular.mermaid.spec.MermaidSpec
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.TaskProvider

internal fun Project.registerMermaidTrunkTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpec,
  generateLegend: TaskProvider<Task>,
) {
  // TBC
}

internal fun Project.registerMermaidLeafTasks(
  extension: ModularExtensionImpl,
  spec: MermaidSpec,
  file: RegularFile,
) {
  // TBC
}
