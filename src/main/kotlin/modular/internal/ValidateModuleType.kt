/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.ModuleType
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

internal fun validate(type: ModuleType, logger: Logger, throwIfInvalid: Boolean) = with(type) {
  if (!color.isPresent) {
    logger.throwOrLog(throwIfInvalid, "No color specified for module type $name")
  }

  val propertyPresences = mapOf(
    "pathContains" to pathContains.isPresent,
    "pathMatches" to pathMatches.isPresent,
    "hasPluginId" to hasPluginId.isPresent,
  )
  val propertyKeys = propertyPresences.keys.joinToString()

  val presentProperties = propertyPresences.filter { (_, isPresent) -> isPresent }
  when {
    presentProperties.isEmpty() ->
      logger.throwOrLog(throwIfInvalid, "You need to specify one of [$propertyKeys] for module type $name")

    presentProperties.size > 1 ->
      logger.throwOrLog(throwIfInvalid, "You can only specify one of [$propertyKeys] for module type $name")
  }
}

private fun Logger.throwOrLog(throwifInvalid: Boolean, message: String) {
  if (throwifInvalid) throw GradleException(message) else warn(message)
}
