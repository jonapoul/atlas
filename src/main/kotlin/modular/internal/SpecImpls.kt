/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.ModularExtension
import modular.spec.ArrowType
import modular.spec.Dir
import modular.spec.DotFileChartSpec
import modular.spec.DotFileLegendSpec
import modular.spec.DotFileOutputFormatSpec
import modular.spec.DotFileSpec
import modular.spec.ExperimentalSpec
import modular.spec.LayoutEngine
import modular.spec.ModulePathTransformSpec
import modular.spec.ModuleType
import modular.spec.OutputSpec
import modular.spec.RankDir
import modular.spec.Spec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

internal open class ModularExtensionImpl @Inject constructor(
  private val objects: ObjectFactory,
  project: Project,
) : ModularExtension {
  internal val properties = ModularProperties(project)

  override val experimental = ExperimentalSpecImpl(objects, properties)
  override fun experimental(action: Action<ExperimentalSpec>) = action.execute(experimental)

  override val modulePathTransforms = ModulePathTransformSpecImpl(objects)
  override fun modulePathTransforms(action: Action<ModulePathTransformSpec>) = action.execute(modulePathTransforms)

  override val moduleTypes = ModuleTypeContainer(objects)
  override fun moduleTypes(action: Action<NamedDomainObjectContainer<ModuleType>>) = action.execute(moduleTypes)

  override val outputs = OutputSpecImpl(objects, project)
  override fun outputs(action: Action<OutputSpec>) = action.execute(outputs)

  override val specs: NamedDomainObjectContainer<Spec<*, *>> = objects.domainObjectContainer(Spec::class.java)
  override fun specs(action: Action<NamedDomainObjectContainer<Spec<*, *>>>) = action.execute(specs)

  override val generateOnSync = objects.bool(properties.generateOnSync)
  override val ignoredConfigs = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  override val ignoredModules = objects.set(convention = emptySet<Regex>())
  override val separator = objects.string(properties.separator)
  override val supportUpwardsTraversal = objects.bool(properties.supportUpwardsTraversal)

  override fun dotFile() = dotFile { /* No-op */ }
  override fun dotFile(action: Action<DotFileSpec>) {
    val spec = specs.findByName(DotFileSpecImpl.NAME)
      as? DotFileSpec
      ?: DotFileSpecImpl(objects, properties)
    action.execute(spec)
    specs.add(spec)
  }

  internal companion object {
    internal const val NAME = "modular"
  }
}

internal class ExperimentalSpecImpl(objects: ObjectFactory, properties: ModularProperties) : ExperimentalSpec {
  override val adjustSvgViewBox = objects.bool(convention = properties.adjustSvgViewBox)
}

internal class ModulePathTransformSpecImpl(objects: ObjectFactory) :
  ModulePathTransformSpec,
  SetProperty<Replacement> by objects.setProperty(Replacement::class.java) {
  override fun replace(pattern: Regex, replacement: String) = add(Replacement(pattern, replacement))
  override fun replace(pattern: String, replacement: String) = replace(pattern.toRegex(), replacement)
  override fun remove(pattern: Regex) = replace(pattern, replacement = "")
  override fun remove(pattern: String) = remove(pattern.toRegex())
}

internal abstract class ModuleTypeImpl @Inject constructor(override val name: String) : ModuleType {
  @get:Input abstract override val color: Property<String>
  @get:Input abstract override val pathContains: Property<String>
  @get:Input abstract override val pathMatches: Property<Regex>
  @get:Input abstract override val hasPluginId: Property<String>

  init {
    color.convention("#FFFFFF")
    pathContains.unsetConvention()
    pathMatches.unsetConvention()
    hasPluginId.unsetConvention()
  }
}

internal class OutputSpecImpl(objects: ObjectFactory, project: Project) : OutputSpec {
  private val projectDir = project.layout.projectDirectory

  // chartOutputDirectory is a bit of a hack - when this gets configured it'll store the directory relative to the
  // root project. But when we access it in the leaf tasks, we'll manually rewire it to be relative to the submodule.
  // Just a workaround to avoid the awkwardness of configuring something relative to each submodule's directory,
  // without breaking config cache restrictions. This rewiring doesn't happen for legendOutputDirectory, since that's
  // always relative to root project.
  internal val chartOutputDirectory = objects.directory(convention = projectDir)
  internal val legendOutputDirectory = objects.directory(convention = projectDir)

  override val chartRootFilename = objects.string(convention = "modules")
  override val legendRootFilename = objects.string(convention = "legend")

  // All chart files will be placed in each submodule's root folder
  override fun saveChartsInSubmoduleDir() = saveChartsRelativeToSubmodule("")

  // All chart files will be placed in the specified relative path to each submodule's root folder
  override fun saveChartsRelativeToSubmodule(relativeToSubmodule: String) =
    chartOutputDirectory.set(projectDir.dir(relativeToSubmodule))

  // All legend files will be placed in the root project's root folder
  override fun saveLegendsInRootDir() = saveLegendsRelativeToRootModule("")

  // All legend files will be placed in the specified path relative to the root module's root folder
  override fun saveLegendsRelativeToRootModule(relativeToRoot: String) =
    legendOutputDirectory.set(projectDir.dir(relativeToRoot))
}

internal class DotFileSpecImpl(
  private val objects: ObjectFactory,
  private val properties: ModularProperties,
) : DotFileSpec {
  override val name = NAME
  override val extension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()

  override var legend: DotFileLegendSpec? = null
  override fun legend() = getOrBuildLegend()
  override fun legend(action: Action<DotFileLegendSpec>) = action.execute(getOrBuildLegend())

  override val chart = DotFileChartSpecImpl(objects, properties)

  override val fileFormats = DotFileOutputFormatSpecImpl(objects)
  override fun fileFormats(action: Action<DotFileOutputFormatSpec>) = action.execute(fileFormats)

  private fun getOrBuildLegend() = legend ?: DotFileLegendSpecImpl(objects, properties).also { legend = it }

  internal companion object {
    internal const val NAME = "DotFileSpecImpl"
  }
}

internal class DotFileLegendSpecImpl(objects: ObjectFactory, properties: ModularProperties) : DotFileLegendSpec {
  override val cellBorder = objects.int(properties.cellBorder)
  override val cellPadding = objects.int(properties.cellPadding)
  override val cellSpacing = objects.int(properties.cellSpacing)
  override val tableBorder = objects.int(properties.tableBorder)
}

internal class DotFileChartSpecImpl(objects: ObjectFactory, properties: ModularProperties) : DotFileChartSpec {
  override fun arrowHead(type: ArrowType) = arrowHead(type.string)
  override fun arrowHead(type: String) = arrowHead.set(type)
  override fun arrowTail(type: ArrowType) = arrowTail(type.string)
  override fun arrowTail(type: String) = arrowTail.set(type)
  override fun dir(dir: Dir) = dir(dir.string)
  override fun dir(dir: String) = this.dir.set(dir)
  override fun layoutEngine(layoutEngine: LayoutEngine) = layoutEngine(layoutEngine.string)
  override fun layoutEngine(layoutEngine: String) = this.layoutEngine.set(layoutEngine)
  override fun rankDir(rankDir: RankDir) = rankDir(rankDir.string)
  override fun rankDir(rankDir: String) = this.rankDir.set(rankDir)
  override val arrowHead = objects.string(properties.arrowHead)
  override val arrowTail = objects.string(properties.arrowTail)
  override val dir = objects.string(properties.dir)
  override val dpi = objects.int(properties.dpi)
  override val fontSize = objects.int(properties.fontSize)
  override val layoutEngine = objects.string(properties.layoutEngine)
  override val rankDir = objects.string(properties.rankDir)
  override val rankSep = objects.float(properties.rankSep)
}

internal class DotFileOutputFormatSpecImpl(objects: ObjectFactory) :
  DotFileOutputFormatSpec,
  SetProperty<String> by objects.setProperty(String::class.java)
