package atlas.core.internal

import atlas.core.tasks.AtlasGenerationTask
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal const val ATLAS_TASK_GROUP: String = "atlas"

internal fun AtlasGenerationTask.logIfConfigured(file: File) {
  if (printFilesToConsole.get()) {
    logger.lifecycle(file.absolutePath)
  }
}

internal interface DummyAtlasGenerationTask : AtlasGenerationTask

internal val KClass<out AtlasGenerationTask>.qualifier: String
  get() =
    when {
      isSubclassOf(DummyAtlasGenerationTask::class) -> "Dummy"
      else -> ""
    }
