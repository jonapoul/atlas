/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.tasks

import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

public interface TaskWithOutputFile : Task {
  @get:OutputFile public val outputFile: RegularFileProperty
}

public interface AtlasGenerationTask : Task {
  @get:Input public val printFilesToConsole: Property<Boolean>
}
