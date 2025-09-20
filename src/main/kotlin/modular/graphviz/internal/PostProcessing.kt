/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import java.io.File

internal fun doGraphVizPostProcessing(outputFile: File, outputFormat: String, adjustSvgViewBox: Boolean) {
  val format = outputFormat.lowercase()
  when {
    format == "svg" && adjustSvgViewBox -> editSvgViewBoxToMatchSize(outputFile)
  }
}

/**
 * Manually editing the viewBox to match the width/height specified in the SVG header element. This only needs to be
 * done if the DPI has been set to a non-default value (default is 72). Example header:
 *
 * ```xml
 * <svg width="486pt" height="661pt" viewBox="0.00 0.00 350.00 476.00" ...>
 * ```
 *
 * Updated to:
 *
 * ```xml
 * <svg width="486pt" height="661pt" viewBox="0.00 0.00 486.00 661.00" ...>
 * ```
 *
 * See DotFileBigGraph100DpiSvg in the tests.
 */
private fun editSvgViewBoxToMatchSize(svgFile: File) {
  val svgContents = svgFile.readText()

  val svgHeader = "<svg width=\"(\\d+)pt\" height=\"(\\d+)pt\""
    .toRegex()
    .find(svgContents)
    ?.groupValues
    ?: error("No svg header found in $svgContents")

  val width = svgHeader[1].toFloat()
  val height = svgHeader[2].toFloat()

  val newViewBox = "viewBox=\"0.00 0.00 %.2f %.2f\"".format(width, height)
  val newSvgContents = svgContents.replace(
    regex = "viewBox=\"(.*?)\"".toRegex(),
    replacement = newViewBox,
  )

  svgFile.writeText(newSvgContents)
}
