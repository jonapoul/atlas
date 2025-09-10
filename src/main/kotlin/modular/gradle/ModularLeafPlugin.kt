/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.configureSeparators
import modular.internal.registerGenerationTaskOnSync
import modular.spec.DotFileOutputSpec
import modular.spec.OutputSpec
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
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

    extension.outputs.configureEach { spec ->
      val file = outputFile(spec)
      when (spec) {
        is DotFileOutputSpec -> registerDotFileTasks(extension, spec, file)
        else -> error("Unexpected output spec $spec")
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

  private fun Project.registerDotFileTasks(extension: ModularExtension, spec: DotFileOutputSpec, file: RegularFile) {
    GenerateModulesDotFileTask.register(
      target = this,
      name = GenerateModulesDotFileTask.TASK_NAME,
      moduleNames = extension.moduleNames,
      spec = spec.chart,
      dotFile = file,
      printOutput = true,
    )

    //    val generateTempDotFileTask = GenerateModulesDotFileTask.register(
    //      target = this,
    //      name = "generateTempDotFile",
    //      chartSpec = TBC,
    //      dotFile = outputDirectory.map { it.file("modules-temp.dot") },
    //      printOutput = false,
    //    )
  }

  private fun Project.outputFile(spec: OutputSpec<*, *>): RegularFile {
    val relative = spec.chart
      .file
      .get()
      .asFile
      .relativeTo(rootProject.projectDir)
      .path
    return layout.projectDirectory.file(relative)
  }
}
