package atlas.core.internal

import atlas.core.InternalAtlasApi
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

@InternalAtlasApi
public fun Provider<RegularFile>.withExtension(
  project: Project,
  extension: Provider<Any>,
): Provider<RegularFile> = zip(extension) { f, ext ->
  val newFile = f.asFile.resolveSibling("${f.asFile.nameWithoutExtension}.$ext")
  project.layout.projectDirectory.file(newFile.relativeTo(project.projectDir).path)
}
