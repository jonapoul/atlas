package atlas.test

import org.junit.jupiter.api.io.TempDir
import java.io.File

@Suppress("AbstractClassCanBeConcreteClass")
internal abstract class ScenarioTest {
  @TempDir lateinit var projectRoot: File

  protected fun <T> runScenario(scenario: Scenario, test: File.() -> T) {
    val settingsFile = """
      ${if (scenario.isGroovy) REPOSITORIES_GRADLE_GROOVY else REPOSITORIES_GRADLE_KTS}
      ${scenario.includeStatements()}
    """.trimIndent()

    with(projectRoot) {
      resolve(scenario.settingsFileName).writeText(settingsFile)
      resolve(scenario.buildFileName).writeText(scenario.rootBuildFile)
      resolve("gradle.properties").writeText(scenario.gradlePropertiesFile)

      scenario.subprojectBuildFiles.forEach { (path, contents) ->
        resolve(projectPathToFilePath(path))
          .also { it.mkdirs() }
          .resolve(scenario.buildFileName)
          .writeText(contents)
      }
      test()
    }
  }

  private fun projectPathToFilePath(projectPath: String): String = projectPath
    .split(":")
    .filter { it.isNotEmpty() }
    .joinToString(separator = File.separator)

  private val Scenario.buildFileName get() = if (isGroovy) "build.gradle" else "build.gradle.kts"
  private val Scenario.settingsFileName get() = if (isGroovy) "settings.gradle" else "settings.gradle.kts"

  private fun Scenario.includeStatements() =
    subprojectBuildFiles.keys.joinToString(separator = "\n") { name ->
      if (isGroovy) {
        "include(':$name')"
      } else {
        "include(\":$name\")"
      }
    }
}
