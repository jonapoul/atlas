/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.FILENAME_ROOT
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModularLeafPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      error("ModularLeafPlugin is not meant to be applied on a non-root project - you applied it to $path")
    }

    val extension = rootProject.extensions.getByType(ModularExtension::class.java)

    val realDotFile = layout.projectDirectory.file("$FILENAME_ROOT.dot")

    DumpModuleTypeTask.register(project, extension)
    DumpModuleLinksTask.register(project, extension)

    CalculateModuleTreeTask.register(project, extension)

    //
    //    val generateModulesDotFileTask = GenerateModulesDotFileTask.register(
    //      target = project,
    //      name = GenerateModulesDotFileTask.TASK_NAME,
    //      dotFile = provider { realDotFile },
    //      printOutput = true,
    //    )
    //
    //    val generateTempDotFileTask = GenerateModulesDotFileTask.register(
    //      target = project,
    //      name = "generateTempDotFile",
    //      dotFile = layout.buildDirectory.file("diagrams-modules-temp/$FILENAME_ROOT.dot"),
    //      printOutput = false,
    //    )
    //
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
}
