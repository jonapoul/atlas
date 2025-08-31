package modular.internal

import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal fun Project.gradleBoolProperty(key: String, default: Boolean): Provider<Boolean> =
  providers
    .gradleProperty(key)
    .map { it.toBooleanStrict() }
    .orElse(default)

internal fun Project.gradleStringProperty(key: String, default: String): Provider<String> =
  providers
    .gradleProperty(key)
    .orElse(default)
