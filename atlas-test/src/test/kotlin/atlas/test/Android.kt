/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test

import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Assumptions.assumeTrue
import java.io.File

internal fun androidHomeOrSkip(): File {
  val androidHome = System.getProperty("test.androidHome")
  assumeFalse(androidHome.isNullOrBlank())
  val androidHomeFile = File(androidHome)
  assumeTrue(androidHomeFile.exists())
  return androidHomeFile
}
