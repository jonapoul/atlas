/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.tasks.ModularGenerationTask
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@InternalModularApi
const val MODULAR_TASK_GROUP = "modular"

@InternalModularApi
fun ModularGenerationTask.logIfConfigured(file: File) {
  if (printFilesToConsole.get()) {
    logger.lifecycle(file.absolutePath)
  }
}

@InternalModularApi
interface DummyModularGenerationTask : ModularGenerationTask

@InternalModularApi
val KClass<out ModularGenerationTask>.qualifier: String
  get() = when {
    isSubclassOf(DummyModularGenerationTask::class) -> "Dummy"
    else -> ""
  }
