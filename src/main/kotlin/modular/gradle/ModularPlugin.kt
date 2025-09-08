/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.FILENAME_ROOT
import modular.internal.orderedTypes
import modular.spec.DotFileOutputSpec
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
import modular.tasks.GenerateLegendDotFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName

class ModularPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      applyToRoot()
    } else {
      applyToSubModule()
    }
  }

  private fun Project.applyToRoot() {
    val extension = extensions.create<ModularExtension>(EXTENSION_NAME)

    CollateModuleTypesTask.register(project, extension)
    CollateModuleLinksTask.register(project, extension)

    extension.outputs.configureEach { outputConfig ->
      when (outputConfig) {
        is DotFileOutputSpec -> GenerateLegendDotFileTask.register(
          target = project,
          config = outputConfig,
          extension = extension,
        )

        else -> error("Unknown output config $outputConfig")
      }
    }

    //      GeneratePngFileTask.registerLegend(project, generateLegend)

    afterEvaluate {
      if (extension.applyToSubprojects.get()) {
        subprojects { p ->
          p.pluginManager.apply("dev.jonpoulton.modular")
        }
      }

      val types = extension.orderedTypes()
      if (types.isEmpty()) {
        logger.warn("Warning: No module types have been registered!")
      }

      types.forEach { type ->
        if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
          logger.warn(
            "Warning: Module type '${type.name}' will be ignored - you need to set one of " +
              "pathContains, pathMatches or hasPluginId.",
          )
        }
      }
    }
  }

  private fun Project.applyToSubModule() {
    val extension = rootProject.extensions.getByName<ModularExtension>(EXTENSION_NAME)

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

  private companion object {
    const val EXTENSION_NAME = "modular"
  }
}
