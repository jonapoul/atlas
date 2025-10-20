@file:Suppress("unused")

package atlas.test

import java.io.File

internal fun File.allFilesSorted() = walkTopDown()
  .filter { it.isFile }
  .sorted()
  .toList()
