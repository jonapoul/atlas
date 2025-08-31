package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModuleLink
import modular.internal.ModuleLinks
import modular.internal.SUPPORT_UPWARDS_TRAVERSAL
import modular.internal.fileInReportDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.Project
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
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

@CacheableTask
abstract class CalculateModuleTreeTask : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val collatedLinks: RegularFileProperty
  @get:Input abstract val supportUpwardsTraversal: Property<Boolean>
  @get:Input abstract val thisPath: Property<String>
  @get:Input abstract val separator: Property<String>
  @get:OutputFile abstract val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the tree of module links"
  }

  @TaskAction
  fun execute() {
    val collatedLinksFile = collatedLinks.get().asFile
    val thisPath = thisPath.get()
    val separator = separator.get()
    val supportUpwardsTraversal = supportUpwardsTraversal.get()

    val allLinks = ModuleLinks.read(collatedLinksFile, separator)
    val tree = mutableSetOf<ModuleLink>()

    if (supportUpwardsTraversal) {
      calculate(thisPath, Direction.Up, allLinks, tree)
    }
    calculate(thisPath, Direction.Down, allLinks, tree)

    val outputFile = outputFile.get().asFile
    ModuleLinks.write(tree, outputFile, separator)
  }

  private enum class Direction { Up, Down }

  private fun calculate(
    targetPath: String,
    direction: Direction,
    allLinks: Set<ModuleLink>,
    output: MutableSet<ModuleLink>,
  ) {
    when (direction) {
      Direction.Up -> {
        val relevantLinks = allLinks.filter { it.toPath == targetPath }
        output += relevantLinks
        for (link in relevantLinks) {
          calculate(link.fromPath, direction, allLinks, output)
        }
      }

      Direction.Down -> {
        val relevantLinks = allLinks.filter { it.fromPath == targetPath }
        output += relevantLinks
        for (link in relevantLinks) {
          calculate(link.toPath, direction, allLinks, output)
        }
      }
    }
  }

  companion object {
    private const val NAME = "calculateModuleTree"

    fun get(target: Project): TaskProvider<CalculateModuleTreeTask> =
      target.tasks.named<CalculateModuleTreeTask>(NAME)

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<CalculateModuleTreeTask> = with(target) {
      val task = tasks.register<CalculateModuleTreeTask>(NAME) {
        thisPath.set(target.path)
        separator.set(extension.separator)
        supportUpwardsTraversal.set(supportUpwards(extension))
        outputFile.set(fileInReportDirectory("module-tree"))
      }

      gradle.projectsEvaluated {
        val collateProjectLinks = CollateModuleLinksTask.get(rootProject)
        task.configure { t ->
          t.collatedLinks.set(collateProjectLinks.map { it.outputFile.get() })
          t.dependsOn(collateProjectLinks)
        }
      }

      task
    }

    private fun Project.supportUpwards(extension: ModularExtension): Provider<Boolean> {
      val propertyProvider = providers
        .gradleProperty(SUPPORT_UPWARDS_TRAVERSAL)
        .map { it.toBoolean() }
      return extension.supportUpwardsTraversal.orElse(propertyProvider)
    }
  }
}
