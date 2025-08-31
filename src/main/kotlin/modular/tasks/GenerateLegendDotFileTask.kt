package modular.tasks

import modular.gradle.ModularExtension
import modular.gradle.ModuleTypeModel
import modular.internal.MODULAR_TASK_GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

@CacheableTask
abstract class GenerateLegendDotFileTask : DefaultTask() {
  @get:Input abstract val tableBorder: Property<Int>
  @get:Input abstract val cellBorder: Property<Int>
  @get:Input abstract val cellSpacing: Property<Int>
  @get:Input abstract val cellPadding: Property<Int>
  @get:Input abstract val moduleTypes: ListProperty<ModuleTypeModel>
  @get:OutputFile abstract val dotFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the legend for a project dependency graph."
  }

  @TaskAction
  fun execute() {
    val tb = tableBorder.get()
    val cb = cellBorder.get()
    val cs = cellSpacing.get()
    val cp = cellPadding.get()

    val dotFileContents = buildString {
      appendLine("digraph G {")
      appendLine("node [shape=plaintext]")
      appendLine("table1 [label=<")
      appendLine("<TABLE BORDER=\"$tb\" CELLBORDER=\"$cb\" CELLSPACING=\"$cs\" CELLPADDING=\"$cp\">")
      moduleTypes.get().forEach { type ->
        appendLine("<TR><TD>${type.name}</TD><TD BGCOLOR=\"${type.color}\">module-name</TD></TR>")
      }
      appendLine("</TABLE>")
      appendLine(">];")
      appendLine("}")
    }

    dotFile.get().asFile.writeText(dotFileContents)
  }

  companion object {
    const val TASK_NAME: String = "generateLegendDotFile"
    const val DOT_PATH: String = "docs/legend/legend.dot"

    fun get(target: Project): TaskProvider<GenerateLegendDotFileTask> =
      target.tasks.named<GenerateLegendDotFileTask>(TASK_NAME)

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<GenerateLegendDotFileTask> = with(target) {
      tasks.register<GenerateLegendDotFileTask>(TASK_NAME) {
        //        moduleTypes.set(types)
        dotFile.set(provider { target.layout.projectDirectory.file(DOT_PATH) })
      }
    }
  }
}
