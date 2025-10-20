package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.tasks.AtlasGenerationTask
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@InternalAtlasApi
public const val ATLAS_TASK_GROUP: String = "atlas"

@InternalAtlasApi
public fun AtlasGenerationTask.logIfConfigured(file: File) {
  if (printFilesToConsole.get()) {
    logger.lifecycle(file.absolutePath)
  }
}

@InternalAtlasApi
public interface DummyAtlasGenerationTask : AtlasGenerationTask

@InternalAtlasApi
public val KClass<out AtlasGenerationTask>.qualifier: String
  get() = when {
    isSubclassOf(DummyAtlasGenerationTask::class) -> "Dummy"
    else -> ""
  }
