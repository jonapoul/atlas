/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

internal val Project.outputDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("modular")

internal fun Project.fileInReportDirectory(path: String): Provider<RegularFile> =
  outputDirectory.map { it.file(path) }
