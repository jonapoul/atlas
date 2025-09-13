/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.tasks.CheckDotFileTask
import modular.graphviz.tasks.GenerateGraphvizFileTask
import modular.graphviz.tasks.GenerateModulesDotFileTask
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.configureSeparators
import modular.internal.modularBuildDirectory
import modular.internal.outputFile
import modular.internal.registerGenerationTaskOnSync
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
import modular.tasks.MODULAR_TASK_GROUP
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

class ModularLeafPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      error("ModularLeafPlugin is only meant to be applied on a non-root project!")
    }

    val extension = rootProject.extensions.getByType(ModularExtension::class.java) as ModularExtensionImpl

    configureSeparators(extension)
    registerGenerationTaskOnSync(extension)

    DumpModuleTypeTask.register(project, extension)
    DumpModuleLinksTask.register(project, extension)
    CalculateModuleTreeTask.register(project, extension)

    extension.specs.configureEach { spec ->
      val file = outputFile(extension.outputs, Variant.Modules, fileExtension = spec.fileExtension.get())
      when (spec) {
        is GraphVizSpec -> registerGraphVizTasks(extension, spec, file)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }
  }

  private fun Project.registerGraphVizTasks(extension: ModularExtensionImpl, spec: GraphVizSpec, file: RegularFile) {
    val dotFileTask = GenerateModulesDotFileTask.register(
      target = this,
      name = GenerateModulesDotFileTask.TASK_NAME,
      modulePathTransforms = extension.modulePathTransforms,
      spec = spec,
      outputFile = file,
      printOutput = true,
    )

    val tempDotFileTask = GenerateModulesDotFileTask.register(
      target = this,
      name = GenerateModulesDotFileTask.TASK_NAME_FOR_CHECKING,
      modulePathTransforms = extension.modulePathTransforms,
      spec = spec,
      outputFile = modularBuildDirectory.get().file("modules-temp.dot"),
      printOutput = false,
    )

    val checkTask = CheckDotFileTask.register(
      target = this,
      name = CheckDotFileTask.NAME_MODULES,
      generateDotFile = tempDotFileTask,
      realDotFile = file,
    )

    tasks.maybeCreate("check").dependsOn(checkTask)

    val outputTasks = GenerateGraphvizFileTask.register(
      target = this,
      extension = extension,
      spec = spec,
      variant = Variant.Modules,
      dotFileTask = dotFileTask,
    )

    tasks.register("generateModules") { t ->
      t.group = MODULAR_TASK_GROUP
      t.description = "Wrapper task for the other 'generateModulesX' tasks"
      t.dependsOn(dotFileTask)
      t.dependsOn(outputTasks)
    }
  }
}
