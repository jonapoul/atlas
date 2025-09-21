package modular.mermaid.internal

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Legend
import modular.core.internal.modularBuildDirectory
import modular.core.tasks.CheckFileDiff
import modular.core.tasks.TaskRegistrar
import modular.core.tasks.defaultOutputFile
import modular.mermaid.tasks.WriteMarkdownLegend
import org.gradle.api.Project

internal object MermaidTrunkTaskRegistrar : TaskRegistrar<MermaidSpecImpl> {
  override fun invoke(
    target: Project,
    extension: ModularExtensionImpl,
    spec: MermaidSpecImpl,
  ): Unit = with(target) {
    val realTask = WriteMarkdownLegend.register(
      target = target,
      name = WriteMarkdownLegend.TASK_NAME,
      extension = extension,
      outputFile = defaultOutputFile(extension, spec).map { f ->
        // just to change the extension! right faff
        val file = f.asFile
        val siblingFile = file.resolveSibling("${file.nameWithoutExtension}.md")
        layout.projectDirectory.file(siblingFile.relativeTo(projectDir).path)
      },
    )

    val dummyTask = WriteMarkdownLegend.register(
      target = target,
      name = WriteMarkdownLegend.TASK_NAME_FOR_CHECKING,
      extension = extension,
      outputFile = modularBuildDirectory.map { it.file("legend-temp.md") },
    )

    val checkTask = CheckFileDiff.register(
      target = target,
      variant = Legend,
      spec = spec,
      realTask = realTask,
      dummyTask = dummyTask,
    )

    tasks.maybeCreate("check").dependsOn(checkTask)
  }
}
