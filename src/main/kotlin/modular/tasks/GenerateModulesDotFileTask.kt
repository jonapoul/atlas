package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.ModuleLink
import modular.internal.ModuleLinks
import modular.internal.REMOVE_MODULE_PREFIX
import modular.internal.REPLACEMENT_MODULE_PREFIX
import modular.internal.TypedModule
import modular.internal.TypedModules
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register

@CacheableTask
abstract class GenerateModulesDotFileTask : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val moduleTypesFile: RegularFileProperty
  @get:Input abstract val separator: Property<String>
  @get:Input abstract val thisPath: Property<String>
  @get:Input abstract val printOutput: Property<Boolean>
  @get:Input abstract val toRemove: Property<String>
  @get:Input abstract val replacement: Property<String>
  @get:OutputFile abstract val dotFile: RegularFileProperty

  @TaskAction
  fun action() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val separator = separator.get()
    val thisPath = thisPath.get().cleaned()
    val links = ModuleLinks.read(linksFile, separator)
    val typedModules = TypedModules.read(moduleTypesFile, separator)

    val dotFileContents = buildString {
      appendLine("digraph {")
      appendHeader()
      appendNodes(typedModules, links, thisPath)
      appendLinks(links)
      appendLine("}")
    }

    val dotFile = dotFile.get().asFile
    dotFile.writeText(dotFileContents)

    if (printOutput.get()) {
      logger.lifecycle(dotFile.absolutePath)
    }
  }

  private fun StringBuilder.appendHeader() {
    appendLine("edge [\"dir\"=\"forward\"]")
    appendLine("graph [\"dpi\"=\"100\",\"fontsize\"=\"30\",\"ranksep\"=\"1.5\",\"rankdir\"=\"TB\"]")
    appendLine("node [\"style\"=\"filled\"]")
  }

  private fun StringBuilder.appendNodes(
    typedModules: Set<TypedModule>,
    links: Set<ModuleLink>,
    thisPath: String,
  ) {
    typedModules
      .filter { module -> module in links }
      .map { it.copy(projectPath = it.projectPath.cleaned()) }
      .sortedBy { module -> module.projectPath }
      .forEach { module ->
        val attrs = if (module.projectPath == thisPath) {
          "\"color\"=\"black\",\"penwidth\"=\"3\",\"shape\"=\"box\""
        } else {
          "\"shape\"=\"none\""
        }

        appendLine("\"${module.projectPath}\" [\"fillcolor\"=\"${module.type.color}\",$attrs]")
      }
  }

  private fun StringBuilder.appendLinks(links: Set<ModuleLink>) {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath }, { it.toPath }))
      .forEach { (fromPath, toPath, configuration) ->
        val attrs = if (configuration.contains("implementation", ignoreCase = true)) {
          " [\"style\"=\"dotted\"]"
        } else {
          ""
        }
        appendLine("\"$fromPath\" -> \"$toPath\"$attrs")
      }
  }

  private operator fun Set<ModuleLink>.contains(module: TypedModule): Boolean =
    any { (from, to, _) -> from == module.projectPath || to == module.projectPath }

  private fun String.cleaned() = replace(toRemove.get(), replacement.get())

  companion object {
    const val TASK_NAME: String = "generateModulesDotFile"

    fun register(
      target: Project,
      extension: ModularExtension,
      name: String,
      dotFile: Provider<RegularFile>,
      printOutput: Boolean,
    ): TaskProvider<GenerateModulesDotFileTask> = with(target) {
      val toRemove = providers.gradleProperty(REMOVE_MODULE_PREFIX)
      val replacement = providers.gradleProperty(REPLACEMENT_MODULE_PREFIX)

      val task = tasks.register<GenerateModulesDotFileTask>(name) {
        group = "reporting"
        description = "Generates a project dependency graph for $path"
        this.dotFile.set(dotFile)
        this.toRemove.set(toRemove)
        this.separator.set(extension.separator)
        this.replacement.set(replacement)
        this.printOutput.set(printOutput)
        this.thisPath.set(target.path)
      }

      gradle.projectsEvaluated {
        val collateModuleTypes = CollateModuleTypesTask.get(rootProject)
        val calculateProjectTree = CalculateModuleTreeTask.get(target)
        task.configure { t ->
          t.linksFile.set(calculateProjectTree.map { it.outputFile.get() })
          t.moduleTypesFile.unset()
          //          t.moduleTypesFile.set(collateModuleTypes.map { it.outputFile.get() })
        }
      }

      task
    }
  }
}
