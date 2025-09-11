/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.MODULAR_TASK_GROUP
import modular.internal.configureSeparators
import modular.internal.outputFile
import modular.internal.registerGenerationTaskOnSync
import modular.spec.DotFileSpec
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
import modular.tasks.GenerateGraphvizFileTask
import modular.tasks.GenerateModulesDotFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

class ModularLeafPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      error("ModularLeafPlugin is only meant to be applied on a non-root project!")
    }

    val extension = rootProject.extensions.getByType(ModularExtension::class.java)
    configureSeparators(extension)
    registerGenerationTaskOnSync(extension)

    DumpModuleTypeTask.register(project, extension)
    DumpModuleLinksTask.register(project, extension)
    CalculateModuleTreeTask.register(project, extension)

    extension.specs.configureEach { spec ->
      val file = outputFile(extension.outputs, Variant.Modules, fileExtension = spec.extension.get())
      when (spec) {
        is DotFileSpec -> registerDotFileTasks(extension, spec, file)
      }
    }

    //    GeneratePngFileTask.registerModules(project, generateModulesDotFileTask)
    //    WriteReadmeTask.register(project)
    //
    //    val checkDotFiles = CheckDotFileTask.register(project, generateTempDotFileTask, realDotFile)
    //
    //    afterEvaluate {
    //      tasks.named("check").configure { check ->
    //        check.dependsOn(checkDotFiles)
    //      }
    //    }
  }

  private fun Project.registerDotFileTasks(extension: ModularExtension, spec: DotFileSpec, file: RegularFile) {
    val dotFileTask = GenerateModulesDotFileTask.register(
      target = this,
      name = GenerateModulesDotFileTask.TASK_NAME,
      moduleNames = extension.moduleNames,
      spec = spec.chart,
      outputFile = file,
      printOutput = true,
    )

    //    val generateTempDotFileTask = GenerateModulesDotFileTask.register(
    //      target = this,
    //      name = "generateTempDotFile",
    //      moduleNames = extension.moduleNames,
    //      chartSpec = spec.chart,
    //      dotFile = modularBuildDirectory.map { it.file("modules-temp.dot") },
    //      printOutput = false,
    //    )

    val outputTasks = GenerateGraphvizFileTask.register(
      target = this,
      extension = extension,
      spec = spec,
      variant = Variant.Modules,
      dotFileTask = dotFileTask,
    )

    if (outputTasks.isNotEmpty()) {
      tasks.register("generateModules") { t ->
        t.group = MODULAR_TASK_GROUP
        t.description = "Wrapper task for the other 'generateModulesX' tasks"
        t.dependsOn(outputTasks)
      }
    }
  }
}
