package modular.internal

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

internal val Project.reportDirectory: Provider<Directory>
  get() = project.layout.buildDirectory.dir("reports/modular")

internal fun Project.fileInReportDirectory(path: String): Provider<RegularFile> =
  reportDirectory.map { it.file(path) }

internal fun Project.dotFile(name: String): Provider<RegularFile> =
  reportDirectory.map { it.file("$name.dot") }
