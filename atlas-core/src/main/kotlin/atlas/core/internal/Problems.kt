@file:Suppress("UnstableApiUsage")

package atlas.core.internal

import org.gradle.api.problems.ProblemGroup
import org.gradle.api.problems.ProblemId

internal val PROBLEM_GROUP: ProblemGroup =
  ProblemGroup.create("atlas-group", "Atlas")

internal fun problemId(id: String, description: String): ProblemId =
  ProblemId.create(id, description, PROBLEM_GROUP)
