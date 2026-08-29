package atlas.d2.tasks

import assertk.assertThat
import atlas.test.D2Scenario
import atlas.test.RequiresImageMagick6
import atlas.test.ScenarioTest
import atlas.test.scenarios.D2Basic
import blueprint.test.allTasksSuccessful
import blueprint.test.assertThatTask
import blueprint.test.buildsSuccessfully
import blueprint.test.childDoesNotExist
import blueprint.test.childExists
import blueprint.test.noTasksFailed
import blueprint.test.taskHadResult
import blueprint.test.tasksHadResult
import blueprint.test.tasksSucceeded
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.test.Test
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
      assertThatTask("svgToPng").buildsSuccessfully().allTasksSuccessful()

      // then both SVG and PNG were output
      assertThat(rootDir).childExists("a/atlas/d2/chart.svg").childExists("a/atlas/d2/chart.png")

      // result is cached
      assertThatTask("svgToPng").buildsSuccessfully().taskHadResult(":a:svgToPng", UP_TO_DATE)
    }

  @Test
  fun `Don't run PNG conversion if converter not specified`() =
    runScenario(UnspecifiedConverter) {
      // when the charts were generated, but the PNGs weren't
      assertThatTask("atlasGenerate")
        .buildsSuccessfully()
        .noTasksFailed()
        .tasksSucceeded(":a:execD2Chart", ":b:execD2Chart", ":c:execD2Chart")
        .tasksHadResult(SKIPPED, ":a:svgToPng", ":b:svgToPng", ":c:svgToPng")

      // then
      assertThat(rootDir)
        .childExists("a/atlas/d2/chart.svg")
        .childDoesNotExist("a/atlas/d2/chart.png")
    }

  @Test
  @RequiresImageMagick6
  fun `Convert SVG to PNG with a scale factor`() =
    runScenario(SpecifiedSvgPngConverterAndScale) {
      // when
      assertThatTask("svgToPng").buildsSuccessfully().allTasksSuccessful()

      // then both SVG and PNG were output
      assertThat(rootDir).childExists("a/atlas/d2/chart.svg").childExists("a/atlas/d2/chart.png")
    }

  @Test
  @RequiresImageMagick6
  fun `Don't run PNG conversion if file format is not SVG`() =
    runScenario(SpecifiedConverterButWrongFormat) {
      // when the charts were generated, but the PNGs weren't
      assertThatTask("atlasGenerate")
        .buildsSuccessfully()
        .noTasksFailed()
        .tasksSucceeded(":a:execD2Chart", ":b:execD2Chart", ":c:execD2Chart")
        .tasksHadResult(SKIPPED, ":a:svgToPng", ":b:svgToPng", ":c:svgToPng")

      // then
      assertThat(rootDir)
        .childExists("a/atlas/d2/chart.txt")
        .childDoesNotExist("a/atlas/d2/chart.png")
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

  private object SpecifiedSvgPngConverterAndScale : D2Scenario by D2Basic {
    override val atlasConfig: String =
      """
      d2 {
        convertSvgToPng(SvgToPng.Converter.ImageMagick6, scale = 0.5f)
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
