/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("UnstableApiUsage")

package modular.core.internal

import org.gradle.api.problems.ProblemGroup
import org.gradle.api.problems.ProblemId

internal val PROBLEM_GROUP: ProblemGroup =
  ProblemGroup.create("modular-group", "Modular")

internal fun problemId(id: String, description: String): ProblemId =
  ProblemId.create(id, description, PROBLEM_GROUP)
