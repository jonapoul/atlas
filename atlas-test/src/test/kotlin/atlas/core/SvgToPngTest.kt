package atlas.core

import assertk.assertThat
import assertk.assertions.exists
import atlas.core.tasks.SvgToPng
import atlas.test.D2Scenario
import atlas.test.GraphvizScenario
import atlas.test.RequiresImageMagick6
import atlas.test.ScenarioTest
import atlas.test.allTasksSuccessful
import atlas.test.doesNotExist
import atlas.test.noTasksFailed
import atlas.test.runTask
import atlas.test.scenarios.D2Basic
import atlas.test.scenarios.GraphvizBasic
import atlas.test.taskHadResult
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.lang.ProcessBuilder.Redirect.PIPE
import kotlin.test.Test

internal class SvgToPngTest : ScenarioTest() {
  @ParameterizedTest
  @MethodSource("converters")
  fun `D2 convert SVG to PNG with specified converter`(
    converter: SvgToPng.Converter,
  ) = runScenario(D2SpecifiedSvgPngConverter(converter)) {
    // given
    assumeConverterIsInstalled(converter)

    // when
    val result = runTask("svgToPng").build()

    // then both SVG and PNG were output
    assertThat(result).allTasksSuccessful()
    assertThat(resolve("a/atlas/chart.svg")).exists()
    assertThat(resolve("a/atlas/chart.png")).exists()

    // result is cached
    assertThat(runTask("svgToPng").build())
      .taskHadResult(":a:svgToPng", UP_TO_DATE)
  }

  @ParameterizedTest
  @MethodSource("converters")
  fun `Graphviz convert SVG to PNG with specified converter`(
    converter: SvgToPng.Converter,
  ) = runScenario(GraphvizSpecifiedSvgPngConverter(converter)) {
    // given
    assumeConverterIsInstalled(converter)

    // when
    val result = runTask("svgToPng").build()

    // then both SVG and PNG were output
    assertThat(result).allTasksSuccessful()
    assertThat(resolve("a/atlas/chart.svg")).exists()
    assertThat(resolve("a/atlas/chart.png")).exists()

    // result is cached
    assertThat(runTask("svgToPng").build())
      .taskHadResult(":a:svgToPng", UP_TO_DATE)
  }

  @Test
  fun `Don't run PNG conversion if converter not specified`() = runScenario(D2UnspecifiedConverter) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(resolve("a/atlas/chart.svg")).exists()
    assertThat(resolve("a/atlas/chart.png")).doesNotExist()

    // and the charts were generated, but the PNGs weren't
    assertThat(result)
      .noTasksFailed()
      .taskHadResult(":a:execD2Chart", SUCCESS)
      .taskHadResult(":b:execD2Chart", SUCCESS)
      .taskHadResult(":c:execD2Chart", SUCCESS)
      .taskHadResult(":a:svgToPng", SKIPPED)
      .taskHadResult(":b:svgToPng", SKIPPED)
      .taskHadResult(":c:svgToPng", SKIPPED)
  }

  @Test
  @RequiresImageMagick6
  fun `Don't run PNG conversion if file format is not SVG`() = runScenario(D2SpecifiedConverterButWrongFormat) {
    // when
    val result = runTask("atlasGenerate").build()

    // then
    assertThat(resolve("a/atlas/chart.txt")).exists()
    assertThat(resolve("a/atlas/chart.png")).doesNotExist()

    // and the charts were generated, but the PNGs weren't
    assertThat(result)
      .noTasksFailed()
      .taskHadResult(":a:execD2Chart", SUCCESS)
      .taskHadResult(":b:execD2Chart", SUCCESS)
      .taskHadResult(":c:execD2Chart", SUCCESS)
      .taskHadResult(":a:svgToPng", SKIPPED)
      .taskHadResult(":b:svgToPng", SKIPPED)
      .taskHadResult(":c:svgToPng", SKIPPED)
  }

  private fun assumeConverterIsInstalled(converter: SvgToPng.Converter) {
    val isWindows = System.getProperty("os.name").contains("win", ignoreCase = true)
    val whichCommand = if (isWindows) "where" else "which"

    val isInstalled = try {
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

  private class D2SpecifiedSvgPngConverter(converter: SvgToPng.Converter) : D2Scenario by D2Basic {
    override val rootBuildFile: String = """
      import atlas.core.tasks.SvgToPng
      import atlas.d2.FileFormat

      plugins {
        kotlin("jvm")
        id("$pluginId")
      }

      atlas {
        d2 {
          convertSvgToPng(SvgToPng.Converter.${converter.name})
          fileFormat = FileFormat.Svg
        }
      }
    """.trimIndent()
  }

  private class GraphvizSpecifiedSvgPngConverter(converter: SvgToPng.Converter) : GraphvizScenario by GraphvizBasic {
    override val rootBuildFile: String = """
      import atlas.core.tasks.SvgToPng
      import atlas.graphviz.FileFormat

      plugins {
        kotlin("jvm")
        id("$pluginId")
      }

      atlas {
        graphviz {
          convertSvgToPng(SvgToPng.Converter.${converter.name})
          fileFormat = FileFormat.Svg
        }
      }
    """.trimIndent()
  }

  private object D2UnspecifiedConverter : D2Scenario by D2Basic {
    override val rootBuildFile: String = """
      import atlas.d2.FileFormat

      plugins {
        kotlin("jvm")
        id("$pluginId")
      }

      atlas {
        d2 {
          fileFormat = FileFormat.Svg
        }
      }
    """.trimIndent()
  }

  private object D2SpecifiedConverterButWrongFormat : D2Scenario by D2Basic {
    override val rootBuildFile: String = """
      import atlas.core.tasks.SvgToPng
      import atlas.d2.FileFormat

      plugins {
        kotlin("jvm")
        id("$pluginId")
      }

      atlas {
        d2 {
          fileFormat = FileFormat.Ascii
          convertSvgToPng(SvgToPng.Converter.ImageMagick7)
        }
      }
    """.trimIndent()
  }

  companion object {
    @JvmStatic
    fun converters() = SvgToPng.Converter.entries
  }
}
