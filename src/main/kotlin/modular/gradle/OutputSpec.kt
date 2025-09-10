/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.directory
import modular.internal.string
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

class OutputSpec(objects: ObjectFactory, project: Project) {
  private val projectDir = project.layout.projectDirectory

  // chartOutputDirectory is a bit of a hack - when this gets configured it'll store the directory relative to the
  // root project. But when we access it in the leaf tasks, we'll manually rewire it to be relative to the submodule.
  // Just a workaround to avoid the awkwardness of configuring something relative to each submodule's directory,
  // without breaking config cache restrictions. This rewiring doesn't happen for legendOutputDirectory, since that's
  // always relative to root project.
  internal val chartOutputDirectory = objects.directory(convention = projectDir)
  internal val legendOutputDirectory = objects.directory(convention = projectDir)

  val chartRootFilename: Property<String> = objects.string(convention = "modules")
  val legendRootFilename: Property<String> = objects.string(convention = "legend")

  // All chart files will be placed in each submodule's root folder
  @ModularDsl fun saveChartsInSubmoduleDir() = saveChartsRelativeToSubmodule(relativeToSubmodule = null)

  // All chart files will be placed in the specified relative path to each submodule's root folder
  @ModularDsl fun saveChartsRelativeToSubmodule(relativeToSubmodule: String?) =
    chartOutputDirectory.set(relativeToSubmodule?.let(projectDir::dir) ?: projectDir)

  // All legend files will be placed in the root project's root folder
  @ModularDsl fun saveLegendsInRootDir() = saveLegendsRelativeToRootModule(relativeToRoot = null)

  // All legend files will be placed in the specified path relative to the root module's root folder
  @ModularDsl fun saveLegendsRelativeToRootModule(relativeToRoot: String?) =
    legendOutputDirectory.set(relativeToRoot?.let(projectDir::dir) ?: projectDir)
}

enum class Variant { Modules, Legend }
