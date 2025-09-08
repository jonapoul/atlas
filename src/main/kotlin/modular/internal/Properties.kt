/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal fun Project.gradleBoolProperty(key: String, default: Boolean): Provider<Boolean> =
  providers
    .gradleProperty(key)
    .map { it.toBooleanStrict() }
    .orElse(default)

internal fun Project.gradleIntProperty(key: String, default: Int): Provider<Int> =
  providers
    .gradleProperty(key)
    .map { it.toInt() }
    .orElse(default)

internal fun Project.gradleStringProperty(key: String, default: String): Provider<String> =
  providers
    .gradleProperty(key)
    .orElse(default)
