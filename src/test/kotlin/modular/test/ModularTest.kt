package modular.test

import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class ModularTest {
  @TempDir lateinit var projectRoot: File

  @BeforeTest
  fun beforeModularTest() {
    basicSettingsFile().copyTo(projectRoot.resolve("settings.gradle.kts"))
  }

  protected fun <T> runScenario(scenario: Scenario, test: File.() -> T) {
    with(projectRoot) {
      resolve("settings.gradle.kts").writeText(scenario.settingsFile)
      resolve("build.gradle.kts").writeText(scenario.rootBuildFile)
      scenario.submoduleBuildFiles.forEach { (path, contents) ->
        resolve(path)
          .also { it.mkdirs() }
          .resolve("build.gradle.kts")
          .writeText(contents)
      }
      test()
    }
  }
}
