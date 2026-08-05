package atlas.test

import blueprint.test.DEFAULT_REPOSITORIES_KTS
import blueprint.test.FileTree
import blueprint.test.Scenario as RunningScenario
import blueprint.test.ScenarioTest as BlueprintScenarioTest
import java.io.File
import kotlin.test.fail
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assumptions.assumeFalse

@Suppress("AbstractClassCanBeConcreteClass")
internal abstract class ScenarioTest : BlueprintScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  private var current: FileTree = fileTree {}

  override val fileTree: FileTree
    get() = current

  protected fun runScenario(
    scenario: Scenario,
    runner: GradleRunner = defaultRunner(),
    test: RunningScenario.() -> Unit,
  ) {
    current = scenario.toFileTree()
    super.runScenario(runner, test)
  }

  protected fun androidRunner(): GradleRunner {
    val home = ANDROID_HOME
    val reason = "No ANDROID_HOME supplied for an android test"
    if (isRunningOnCi()) {
      if (home == null) fail(reason)
    } else {
      assumeFalse(home == null) { "No ANDROID_HOME supplied for an android test" }
    }

    return defaultRunner().withEnvironment(mapOf("ANDROID_HOME" to checkNotNull(home).absolutePath))
  }

  private fun Scenario.toFileTree(): FileTree =
    FileTree.Builder(relativeRootPath = "")
      .apply {
        settingsFileName(settingsFile())
        buildFileName(rootBuildFile)
        "gradle.properties"(gradleProperties())

        subprojectBuildFiles.forEach { (path, contents) ->
          val directory = path.toDirectoryPath()
          directory { buildFileName(contents) }
        }
      }
      .build()

  private fun Scenario.settingsFile() = buildString {
    listOf(
        // TODO: More?
        "atlas.core.internal.*",
        "atlas.core.*",
      )
      .forEach { appendLine("import $it") }

    appendLine(DEFAULT_REPOSITORIES_KTS.trimIndent())
    appendLine()

    if (isGroovy) {
      appendLine("plugins { id '$pluginId' }")
    } else {
      appendLine("plugins { id(\"$pluginId\") }")
    }
    appendLine()

    subprojectBuildFiles.keys.forEach { path ->
      if (isGroovy) appendLine("include(':$path')") else appendLine("include(\":$path\")")
    }
    appendLine()

    appendLine(atlasBlock())
  }

  private fun Scenario.atlasBlock(): String {
    val body =
      frameworks.map { framework -> "  ${framework.string}()" } +
        atlasConfig.trimIndent().lines().filter(String::isNotBlank).map { "  $it" }
    return if (body.isEmpty()) "" else body.joinToString("\n", "atlas {\n", "\n}\n")
  }

  private fun Scenario.gradleProperties() = buildString {
    appendLine("android.useAndroidX=true")
    // Atlas is built to work under isolated projects, so every scenario runs with it enabled by
    // default
    appendLine("org.gradle.unsafe.isolated-projects=true")
    appendLine(gradlePropertiesFile)
  }

  private fun String.toDirectoryPath() =
    split(":").filter(String::isNotEmpty).joinToString(File.separator)

  private val Scenario.buildFileName
    get() = if (isGroovy) "build.gradle" else "build.gradle.kts"

  private val Scenario.settingsFileName
    get() = if (isGroovy) "settings.gradle" else "settings.gradle.kts"
}
