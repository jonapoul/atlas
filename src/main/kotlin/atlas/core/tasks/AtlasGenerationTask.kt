package atlas.core.tasks

import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

public interface AtlasGenerationTask : Task {
  @get:Input public val printFilesToConsole: Property<Boolean>
}
