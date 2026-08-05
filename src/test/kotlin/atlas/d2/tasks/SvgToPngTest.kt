package atlas.d2.tasks

import assertk.assertThat
import atlas.test.D2Scenario
import atlas.test.RequiresImageMagick6
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.childDoesNotExist
import atlas.test.childExists
import atlas.test.noTasksFailed
import atlas.test.scenarios.D2Basic
import atlas.test.tasksHadResult
import blueprint.test.runTask
import blueprint.test.taskHadResult
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.test.Test
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class SvgToPngTest : ScenarioTest() {
  @ParameterizedTest
  @MethodSource("converters")
  fun `Convert SVG to PNG with specified converter`(converter: SvgToPng.Converter) =
    runScenario(SpecifiedSvgPngConverter(converter)) {
      // given
      assumeConverterIsInstalled(converter)

      // when
      val result = runTask("svgToPng").build()

      // then both SVG and PNG were output
      assertThat(result).allTasksSuccessful()
      assertThat(rootDir).childExists("a/atlas/d2/chart.svg").childExists("a/atlas/d2/chart.png")

      // result is cached
      assertThat(runTask("svgToPng").build()).taskHadResult(":a:svgToPng", UP_TO_DATE)
    }

  @Test
  fun `Don't run PNG conversion if converter not specified`() =
    runScenario(UnspecifiedConverter) {
      // when
      val result = runTask("atlasGenerate").build()

      // then
      assertThat(rootDir)
        .childExists("a/atlas/d2/chart.svg")
        .childDoesNotExist("a/atlas/d2/chart.png")

      // and the charts were generated, but the PNGs weren't
      assertThat(result)
        .noTasksFailed()
        .tasksHadResult(SUCCESS, ":a:execD2Chart", ":b:execD2Chart", ":c:execD2Chart")
        .tasksHadResult(SKIPPED, ":a:svgToPng", ":b:svgToPng", ":c:svgToPng")
    }

  @Test
  @RequiresImageMagick6
  fun `Don't run PNG conversion if file format is not SVG`() =
    runScenario(SpecifiedConverterButWrongFormat) {
      // when
      val result = runTask("atlasGenerate").build()

      // then
      assertThat(rootDir)
        .childExists("a/atlas/d2/chart.txt")
        .childDoesNotExist("a/atlas/d2/chart.png")

      // and the charts were generated, but the PNGs weren't
      assertThat(result)
        .noTasksFailed()
        .tasksHadResult(SUCCESS, ":a:execD2Chart", ":b:execD2Chart", ":c:execD2Chart")
        .tasksHadResult(SKIPPED, ":a:svgToPng", ":b:svgToPng", ":c:svgToPng")
    }

  private fun assumeConverterIsInstalled(converter: SvgToPng.Converter) {
    val isWindows = System.getProperty("os.name").contains("win", ignoreCase = true)
    val whichCommand = if (isWindows) "where" else "which"

    val isInstalled =
      try {
        ProcessBuilder(whichCommand, converter.toString())
          .redirectOutput(PIPE)
          .redirectError(PIPE)
          .start()
          .apply { waitFor() }
          .exitValue() == 0
      } catch (_: Exception) {
        false
      }

    assumeTrue(isInstalled, "Converter '$converter' is not installed on this system")
  }

  private class SpecifiedSvgPngConverter(converter: SvgToPng.Converter) : D2Scenario by D2Basic {
    override val atlasConfig: String =
      """
      d2 {
        convertSvgToPng(SvgToPng.Converter.${converter.name})
        fileFormat = FileFormat.Svg
      }
      """
        .trimIndent()
  }

  private object UnspecifiedConverter : D2Scenario by D2Basic {
    override val atlasConfig: String =
      """
      d2 {
        fileFormat = FileFormat.Svg
      }
      """
        .trimIndent()
  }

  private object SpecifiedConverterButWrongFormat : D2Scenario by D2Basic {
    override val atlasConfig: String =
      """
      d2 {
        fileFormat = FileFormat.Ascii
        convertSvgToPng(SvgToPng.Converter.ImageMagick7)
      }
      """
        .trimIndent()
  }

  companion object {
    @JvmStatic fun converters() = SvgToPng.Converter.entries
  }
}
