package atlas.test

import java.io.File
import org.junit.jupiter.api.io.TempDir

@Suppress("AbstractClassCanBeConcreteClass")
internal abstract class ScenarioTest {
  @TempDir lateinit var projectRoot: File

  protected fun <T> runScenario(scenario: Scenario, test: File.() -> T) {
    val settingsFile =
      """
      ${if (scenario.isGroovy) REPOSITORIES_GRADLE_GROOVY else REPOSITORIES_GRADLE_KTS}
      ${scenario.includeStatements()}
    """
        .trimIndent()

    with(projectRoot) {
      resolve(scenario.settingsFileName).writeText(settingsFile)
      resolve(scenario.buildFileName)
        .writeText(scenario.rootBuildFile + scenario.enableFrameworks())
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

  /**
   * A framework only generates diagrams once its block has been configured, so switch on the ones
   * this scenario declared. Configuring the same extension twice is fine, so scenarios which
   * already configure a framework in detail don't need to care.
   */
  private fun Scenario.enableFrameworks(): String =
    if (frameworks.isEmpty()) {
      ""
    } else {
      frameworks.joinToString(
        prefix = "\n\natlas {\n",
        separator = "\n",
        postfix = "\n}\n",
      ) { framework ->
        "  ${framework.string}()"
      }
    }

  private fun projectPathToFilePath(projectPath: String): String =
    projectPath.split(":").filter { it.isNotEmpty() }.joinToString(separator = File.separator)

  private val Scenario.buildFileName
    get() = if (isGroovy) "build.gradle" else "build.gradle.kts"

  private val Scenario.settingsFileName
    get() = if (isGroovy) "settings.gradle" else "settings.gradle.kts"

  private fun Scenario.includeStatements() =
    subprojectBuildFiles.keys.joinToString(separator = "\n") { name ->
      if (isGroovy) {
        "include(':$name')"
      } else {
        "include(\":$name\")"
      }
    }
}
