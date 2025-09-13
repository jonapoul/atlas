/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import kotlin.RequiresOptIn.Level.WARNING
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY

@DslMarker
annotation class ModularDsl

@Target(FUNCTION, CLASS, PROPERTY)
@Retention(RUNTIME)
@RequiresOptIn(level = WARNING)
annotation class ExperimentalModularApi
