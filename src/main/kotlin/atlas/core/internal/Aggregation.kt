package atlas.core.internal

import atlas.core.Framework
import java.io.File
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider

/**
 * Isolated projects forbids reading another project's tasks or extensions, so every file Atlas
 * passes between projects travels as a dependency-resolution artifact instead. Each kind gets its
 * own attribute value, so one project dependency can carry any number of them.
 */
@JvmInline
internal value class AtlasArtifact(private val id: String) {
  val attributeValue: String
    get() = id

  val elementsConfigurationName: String
    get() = "$ATLAS_CONFIGURATION_PREFIX${suffix}Elements"

  val resolvableConfigurationName: String
    get() = "$ATLAS_CONFIGURATION_PREFIX${suffix}Classpath"

  private val suffix: String
    get() = id.split("-").joinToString(separator = "") { it.replaceFirstChar(Char::uppercaseChar) }

  companion object {
    /** One project's own type, produced by [atlas.core.tasks.WriteProjectType]. */
    val ProjectType = AtlasArtifact("project-type")

    /** One project's direct links, produced by [atlas.core.tasks.WriteProjectLinks]. */
    val ProjectLinks = AtlasArtifact("project-links")

    /** Every project's type, produced by [atlas.core.tasks.CollateProjectTypes] on the root. */
    val CollatedTypes = AtlasArtifact("collated-types")

    /** Every project's links, produced by [atlas.core.tasks.CollateProjectLinks] on the root. */
    val CollatedLinks = AtlasArtifact("collated-links")

    /** The class definitions every D2 chart shares, written once on the root. */
    val D2Classes = AtlasArtifact("d2-classes")

    /** The shared legend a framework draws once on the root, and every README links to. */
    fun legend(framework: Framework) = AtlasArtifact("legend-${framework.string}")
  }
}

/**
 * Every configuration Atlas creates starts with this, so that the project dependencies it declares
 * on itself never show up as links in the charts.
 */
internal const val ATLAS_CONFIGURATION_PREFIX = "atlas"

internal val ATLAS_ARTIFACT_ATTRIBUTE =
  Attribute.of("dev.jonpoulton.atlas.artifact", String::class.java)

/** Publishes [file] so that other projects can resolve it as [artifact]. */
internal fun Project.publishAtlasArtifact(
  artifact: AtlasArtifact,
  file: Provider<RegularFile>,
  builtBy: TaskProvider<*>,
) {
  configurations.consumable(artifact.elementsConfigurationName) { configuration ->
    configuration.attributes.attribute(ATLAS_ARTIFACT_ATTRIBUTE, artifact.attributeValue)
  }
  artifacts.add(artifact.elementsConfigurationName, file) { it.builtBy(builtBy) }
}

/**
 * Resolves [artifact] from each project in [fromPaths].
 *
 * The view is lenient because a project may legitimately not publish a given artifact - for example
 * one with no build file, which Atlas leaves out of the graph entirely.
 */
internal fun Project.consumeAtlasArtifact(
  artifact: AtlasArtifact,
  fromPaths: List<String>,
): FileCollection {
  val scopeName = "${artifact.resolvableConfigurationName}Dependencies"
  val scope = configurations.dependencyScope(scopeName)

  val resolvable =
    configurations.resolvable(artifact.resolvableConfigurationName) { configuration ->
      configuration.extendsFrom(scope.get())
      configuration.attributes.attribute(ATLAS_ARTIFACT_ATTRIBUTE, artifact.attributeValue)
    }

  fromPaths.forEach { path ->
    dependencies.add(scopeName, dependencies.project(mapOf("path" to path)))
  }

  return resolvable.get().incoming.artifactView { it.lenient(true) }.files
}

/**
 * The single file this collection resolves to. Atlas aggregates at most one artifact per project
 * per kind, so anything resolved from the root project is always exactly one file.
 */
internal fun FileCollection.singleFile(): Provider<File> = elements.map { locations ->
  locations.single().asFile
}
